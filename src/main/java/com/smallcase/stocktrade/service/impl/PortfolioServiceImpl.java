package com.smallcase.stocktrade.service.impl;

import com.smallcase.stocktrade.dto.PortfolioTradesDTO;
import com.smallcase.stocktrade.dto.ReturnsDTO;
import com.smallcase.stocktrade.entities.Portfolio;
import com.smallcase.stocktrade.entities.Trade;
import com.smallcase.stocktrade.exception.DataNotFoundException;
import com.smallcase.stocktrade.exception.ValidationException;
import com.smallcase.stocktrade.repositories.PortfolioRepository;
import com.smallcase.stocktrade.service.PortfolioService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author Meenal created on 15/03/22 at 12:37 AM
 **/

@Service
@Slf4j
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

  private final PortfolioRepository portfolioRepository;

  /** Here we dont want to pass the sensitive data, and hence we change the data to DTO for others to consume. */
  @Override
  public List<PortfolioTradesDTO> getPortfolioTrades(Long custId)
      throws ValidationException, DataNotFoundException {

    if(Objects.isNull(custId)){
      throw new ValidationException("Customer Id is null", 1001);
    }
    Optional<Portfolio> portfolioGet = portfolioRepository.findByCustomerIdAndActiveIsTrue(custId);
    if(portfolioGet.isPresent()) {
      Portfolio portfolio = portfolioGet.get();
      return convertTradesToDTO(portfolio.getTrades());
    } else {
      throw new DataNotFoundException("No Portfolio Details found for the customer id", 1002);
    }
  }

  /** Returns are calculated as summation of all the securities (avgPrice-currentPrice)* num of stocks they have for the security*/
  @Override
  public ReturnsDTO getReturns(Long custId) throws DataNotFoundException, ValidationException {
    List<PortfolioTradesDTO> portfolioTradesDTOS = getPortfolioTrades(custId);

    Double returns = 0d;
    for(PortfolioTradesDTO portfolioTradesDTO: portfolioTradesDTOS){
      returns += (portfolioTradesDTO.getAvgSharePrice() - 100)* portfolioTradesDTO.getShares();
    }
    ReturnsDTO returnsDTO = new ReturnsDTO();
    returnsDTO.setReturns(returns);
    returnsDTO.setPortfolioTradesDTOS(portfolioTradesDTOS);

    return returnsDTO;
  }

  private List<PortfolioTradesDTO> convertTradesToDTO(List<Trade> trades)
      throws DataNotFoundException {
    if(CollectionUtils.isEmpty(trades)){
      throw new DataNotFoundException("No trades exist for this portfolio", 1003);
    }
    List<PortfolioTradesDTO> portfolioTradesDTOS = new ArrayList<>();

    for(Trade trade : trades){
      if(trade.getActive()){
        PortfolioTradesDTO portfolioTradesDTO = new PortfolioTradesDTO();
        portfolioTradesDTO.setTickerSymbol(trade.getTickerSymbol());
        portfolioTradesDTO.setShares(trade.getShares());
        portfolioTradesDTO.setAvgSharePrice(trade.getAvgSharePrice());

        Double invested = trade.getShares() * trade.getAvgSharePrice();
        portfolioTradesDTO.setInvestedAmount(invested);
        portfolioTradesDTOS.add(portfolioTradesDTO);
      }
    }
    if(CollectionUtils.isEmpty(portfolioTradesDTOS)){
      throw new DataNotFoundException("No active trades exist for this portfolio", 1011);
    }
    return portfolioTradesDTOS;
  }
}
