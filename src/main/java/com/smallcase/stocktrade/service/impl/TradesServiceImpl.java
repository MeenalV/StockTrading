package com.smallcase.stocktrade.service.impl;

import com.smallcase.stocktrade.dto.Action;
import com.smallcase.stocktrade.dto.TradeRequestDTO;
import com.smallcase.stocktrade.dto.TradeResponseDTO;
import com.smallcase.stocktrade.dto.TradeType;
import com.smallcase.stocktrade.entities.Portfolio;
import com.smallcase.stocktrade.entities.Trade;
import com.smallcase.stocktrade.entities.TradeHistory;
import com.smallcase.stocktrade.exception.DataNotFoundException;
import com.smallcase.stocktrade.exception.InvalidActionException;
import com.smallcase.stocktrade.exception.ValidationException;
import com.smallcase.stocktrade.repositories.PortfolioRepository;
import com.smallcase.stocktrade.repositories.TradeHistoryRepository;
import com.smallcase.stocktrade.repositories.TradeRepository;
import com.smallcase.stocktrade.service.TradesService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author Meenal created on 15/03/22 at 1:35 AM
 **/

@Service
@Slf4j
@RequiredArgsConstructor
public class TradesServiceImpl implements TradesService {

  private final PortfolioRepository portfolioRepository;
  private final TradeRepository tradeRepository;
  private final TradeHistoryRepository tradeHistoryRepository;

  /** Here we create new trade. now when we create new trade, we cannot have a tradeId, hence the validation
   * is necessity.
   * By just creating a trade for the first time will lead to creating portfolio from here.
   * after this, any trade made by the user will be added to this portfolio. */
  @Override
  public TradeResponseDTO createNewTrade(Long custId, TradeRequestDTO tradeRequestDTO)
      throws ValidationException, InvalidActionException {
    if (Objects.nonNull(tradeRequestDTO.getId())) {
      throw new ValidationException("When creating a new trade, we cannot have Id", 1004);
    } else {
      Optional<Portfolio> portfolioGet = portfolioRepository.findByCustomerIdAndActiveIsTrue(
          custId);
      TradeResponseDTO tradeResponseDTO = new TradeResponseDTO();
      Trade trade = createTradeDataFromRequest(tradeRequestDTO, custId, tradeResponseDTO);

      Portfolio portfolio;
      if (!portfolioGet.isPresent()) {
        portfolio = new Portfolio();
        portfolio.setTrades(Collections.singletonList(trade));
        portfolio.setCustomerId(custId);
        portfolio.setCreatedBy(custId.toString());
        portfolio.setCreatedAt(new Date());
        tradeRepository.save(trade);

      } else {
        portfolio = portfolioGet.get();
        portfolio.setModifiedAt(new Date());
        portfolio.setModifiedBy(custId.toString());

        List<Trade> trades = portfolio.getTrades();
        trades.add(trade);
        portfolio.setTrades(trades);
        tradeRepository.saveAll(trades);
      }

      portfolio = portfolioRepository.save(portfolio);

      /** Every transaction is recorded in Trade History. Trade has the calculated values after a trade takes place*/
      TradeHistory tradeHistory = new TradeHistory();
      saveTradeAuditData(trade, portfolio, tradeRequestDTO.getTradeType(), Action.CREATED,
          tradeRequestDTO, tradeHistory);
      createTradeResponseDTO(trade, tradeResponseDTO, tradeRequestDTO, tradeHistory);
      return tradeResponseDTO;
    }
  }

  /** after placing a trade, there is high chance that user wants to modify the trade.
   * This function helps consumer to modify the trade request and accordingly receive
   * the new data and update the portfolio.
   * Here, trade Id is very important, otherwise how can one update the trade?
   * */
  @Override
  public TradeResponseDTO updateTrade(Long custId, TradeRequestDTO tradeRequestDTO)
      throws ValidationException, InvalidActionException {
    if (Objects.isNull(tradeRequestDTO.getId())) {
      throw new ValidationException("When updating a trade, we cannot have empty Id", 1005);
    } else {
      Optional<TradeHistory> tradeHistoryGet = tradeHistoryRepository.findByIdAndActiveIsTrue(
          tradeRequestDTO.getId());
      if (!tradeHistoryGet.isPresent()) {
        throw new ValidationException("No trade exist for the given tradeId", 1006);
      } else {
        TradeHistory tradeHistory = tradeHistoryGet.get();
        TradeResponseDTO tradeResponseDTO = new TradeResponseDTO();

        /** Doing this to revert the change that took place before the updation*/
        TradeRequestDTO tradeRequestDTO1 = new TradeRequestDTO();
        createTradeRequestDTOFromHistory(tradeRequestDTO1, tradeHistory);
        createTradeDataFromRequest(tradeRequestDTO1, custId, tradeResponseDTO);

        /** Making the changes with the new updated request here*/
        Trade trade = createTradeDataFromRequest(tradeRequestDTO, custId, tradeResponseDTO);
        Optional<Portfolio> portfolioGet = portfolioRepository.findById(
            tradeHistory.getPortfolioId());
        /** In case with the updated request, a person adds a new security to his portfolio,
         * then that trade data is not present in portfolio, and hence to link that trade to portfolio,
         * we assign portfolio id here to the trade.*/
        if (portfolioGet.isPresent()) {
          trade.setPortfolio(portfolioGet.get());
        } else {
          throw new ValidationException("Portfolio not present", 1009);
        }
        saveTradeAuditData(trade, trade.getPortfolio(), tradeRequestDTO.getTradeType(),
            Action.UPDATED, tradeRequestDTO, tradeHistory);
        tradeRepository.save(trade);

        createTradeResponseDTO(trade, tradeResponseDTO, tradeRequestDTO, tradeHistory);
        return tradeResponseDTO;

      }
    }
  }

  /** If the user wants to remove a trade, or cancel the trade that occurred, they can delete it.
   * This will remove the trade, and with this it will update the portfolio to the state where this trade
   * didnt even take place.*/
  @Override
  public Boolean deleteTrade(Long custId, Long tradeId)
      throws ValidationException, InvalidActionException {
      Optional<TradeHistory> tradeHistoryGet = tradeHistoryRepository.findByIdAndActiveIsTrue(
          tradeId);
      if (!tradeHistoryGet.isPresent()) {
        throw new ValidationException("No trade exist for the given tradeId", 1006);
      } else {
        TradeHistory tradeHistory = tradeHistoryGet.get();
        tradeHistory.setActive(Boolean.FALSE);
        Optional<Trade> tradeGet = tradeRepository.findByCustomerIdAndTickerSymbolAndActiveIsTrue(custId, tradeHistory.getTickerSymbol());
        if(!tradeGet.isPresent()){
          throw new ValidationException("No trade exist for the given tradeId", 1006);
        } else {
          Trade trade = tradeGet.get();
          TradeResponseDTO tradeResponseDTO = new TradeResponseDTO();
          TradeRequestDTO tradeRequestDTO = new TradeRequestDTO();
          
          createTradeRequestDTOFromHistory(tradeRequestDTO, tradeHistory);
          
          updateShareValues(trade, tradeRequestDTO, tradeResponseDTO);

          tradeRepository.save(trade);
          tradeHistoryRepository.save(tradeHistory);
          return Boolean.TRUE;
        }
      }
  }

  /** Here we show all the trades that occurred for the securities in the portfolio.
   * this gives an overall view of the trades executed or removed. */
  @Override
  public Map<String, List<TradeHistory>> getAllTrades(Long custId) throws DataNotFoundException {
    Optional<List<TradeHistory>> tradeHistoriesGet = tradeHistoryRepository.findByCustomerId(custId);
    if(!tradeHistoriesGet.isPresent()){
      throw new DataNotFoundException("No trades made by the user", 1010);
    } else {
      List<TradeHistory> tradeHistories = tradeHistoriesGet.get();
      if(CollectionUtils.isEmpty(tradeHistories)){
        throw new DataNotFoundException("No trades made by the user", 1010);
      }
      Map<String, List<TradeHistory>> trades = tradeHistories.stream()
          .collect(Collectors.groupingBy(TradeHistory::getTickerSymbol,Collectors.toList()));

      return trades;
    }
  }

  private void createTradeRequestDTOFromHistory(TradeRequestDTO tradeRequestDTO, TradeHistory tradeHistory) {
    tradeRequestDTO.setTickerSymbol(tradeHistory.getTickerSymbol());
    tradeRequestDTO.setShares(tradeHistory.getShares());
    tradeRequestDTO.setSharePrice(tradeHistory.getPerSharePrice());
    if(tradeHistory.getTradeType().equalsIgnoreCase(TradeType.SELL.name())){
      tradeRequestDTO.setTradeType(TradeType.BUY);
    } else {
      tradeRequestDTO.setTradeType(TradeType.SELL);
    }
  }

  /** this function simply converts the request values to trade history model to maintain the trades that happened*/
  private void saveTradeAuditData(Trade trade, Portfolio portfolio, TradeType tradeType,
      Action action, TradeRequestDTO tradeRequestDTO,
      TradeHistory tradeHistory) {

    tradeHistory.setCustomerId(trade.getCustomerId());
    tradeHistory.setPortfolioId(portfolio.getId());
    tradeHistory.setTradeId(trade.getId());

    tradeHistory.setTickerSymbol(trade.getTickerSymbol());
    tradeHistory.setShares(tradeRequestDTO.getShares());
    tradeHistory.setTradeType(tradeType.name());
    tradeHistory.setPerSharePrice(tradeRequestDTO.getSharePrice());

    tradeHistory.setAction(action.name());

    tradeHistoryRepository.save(tradeHistory);
  }

  /** After saving all the changes made in the trade, we need to send data to consumer to use.
   * Here we map them to user readable POJO.*/
  private void createTradeResponseDTO(Trade trade, TradeResponseDTO tradeResponseDTO,
      TradeRequestDTO tradeRequestDTO, TradeHistory tradeHistory) {
    tradeResponseDTO.setTradeType(tradeRequestDTO.getTradeType());
    tradeResponseDTO.setTickerSymbol(tradeRequestDTO.getTickerSymbol());
    tradeResponseDTO.setShares(tradeRequestDTO.getShares());
    tradeResponseDTO.setSharePrice(tradeRequestDTO.getSharePrice());

    tradeResponseDTO.setId(tradeHistory.getId());
    tradeResponseDTO.setNewAvgPrice(trade.getAvgSharePrice());
    tradeResponseDTO.setNewSharesCount(trade.getShares());
  }

  /** Using the requestDTO, we need to map it to our model, and this is done here.
   * Whenever a trade takes place, it changes the portfolio data, either by adding new security, or adding shares
   * to the security, or selling the shares. This needs to be calculated and saved.
   * This function creates the model data here. */
  private Trade createTradeDataFromRequest(TradeRequestDTO tradeRequestDTO, Long custId,
      TradeResponseDTO tradeResponseDTO)
      throws InvalidActionException {
    Trade trade;
    Optional<Trade> tradeGet = tradeRepository.findByCustomerIdAndTickerSymbolAndActiveIsTrue(
        custId,
        tradeRequestDTO.getTickerSymbol());
    if (tradeGet.isPresent()) {
      trade = tradeGet.get();
      trade.setModifiedAt(new Date());
      trade.setModifiedBy(custId.toString());
    } else {
      trade = new Trade();
      trade.setTickerSymbol(tradeRequestDTO.getTickerSymbol());
      trade.setCustomerId(custId);
      trade.setCreatedAt(new Date());
      trade.setCreatedBy(custId.toString());
    }

    updateShareValues(trade, tradeRequestDTO, tradeResponseDTO);

    return trade;
  }

  /** Using the requestDTO, we need to map it to our model, and this is done here.
   * Whenever a trade takes place, it changes the portfolio data, either by adding new security, or adding shares
   * to the existing security, or selling the shares (a portfolio). This needs to be calculated and saved.
   * This function calculates the prices and saves the data.
   * It also assigns data for Customer to consume.
   * Given the transaction, if somehow my shares touch zero, we mark the trade as false, because we dont have
   * shares in the security, and hence they should not be shown in the portfolio. */
  private void updateShareValues(Trade trade, TradeRequestDTO tradeRequestDTO,
      TradeResponseDTO tradeResponseDTO)
      throws InvalidActionException {
    tradeResponseDTO.setOldSharesCount(trade.getShares());
    tradeResponseDTO.setOldAvgPrice(trade.getAvgSharePrice());

    if (tradeRequestDTO.getTradeType().equals(TradeType.SELL)) {
      if (trade.getShares() < tradeRequestDTO.getShares()) {
        throw new InvalidActionException("Cannot remove shares more than existing shares", 1008);
      }
      Long currentShares = trade.getShares() - tradeRequestDTO.getShares();
      trade.setShares(currentShares);

    } else {
      Double newTotalPrice = trade.getAvgSharePrice() * trade.getShares()
          + tradeRequestDTO.getSharePrice() * tradeRequestDTO.getShares();

      Long totalShares = trade.getShares() + tradeRequestDTO.getShares();

      Double newAvgPrice = newTotalPrice / totalShares;

      trade.setShares(totalShares);
      trade.setAvgSharePrice(newAvgPrice);
    }

    if(trade.getShares() == 0){
      trade.setActive(Boolean.FALSE);
    }
  }
}
