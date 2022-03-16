package com.smallcase.stocktrade.controller;

import com.smallcase.stocktrade.dto.PortfolioTradesDTO;
import com.smallcase.stocktrade.dto.TradeHistoryDTO;
import com.smallcase.stocktrade.dto.TradeRequestDTO;
import com.smallcase.stocktrade.dto.TradeResponseDTO;
import com.smallcase.stocktrade.entities.TradeHistory;
import com.smallcase.stocktrade.exception.DataNotFoundException;
import com.smallcase.stocktrade.exception.InvalidActionException;
import com.smallcase.stocktrade.exception.ValidationException;
import com.smallcase.stocktrade.service.TradesService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Meenal created on 15/03/22 at 12:28 AM
 **/

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/cloud/frontier/smallcase/trades")
public class TradesController {

  private final TradesService tradesService;

  @PostMapping("v1")
  public TradeResponseDTO createNewTrade(@RequestHeader("customer_id") Long custId,
      @RequestBody TradeRequestDTO tradeRequestDTO)
      throws ValidationException, InvalidActionException {

    log.debug("Entered createNewTrade method for customer : {}", custId);
    TradeResponseDTO tradeResponseDTO = tradesService.createNewTrade(custId, tradeRequestDTO);
    return tradeResponseDTO;
  }

  @PutMapping("v1")
  public TradeResponseDTO updateTrade(@RequestHeader("customer_id") Long custId,
      @RequestBody TradeRequestDTO tradeRequestDTO)
      throws ValidationException, InvalidActionException {

    log.debug("Entered createNewTrade method for customer : {}", custId);
    TradeResponseDTO tradeResponseDTO = tradesService.updateTrade(custId, tradeRequestDTO);
    return tradeResponseDTO;
  }

  @DeleteMapping("v1/{tradeId}")
  public Boolean deleteTrade(@RequestHeader("customer_id") Long custId,
      @PathVariable("tradeId") Long tradeId)
      throws ValidationException, InvalidActionException {

    log.debug("Entered createNewTrade method for customer : {}", custId);
    Boolean tradeResponseDTO = tradesService.deleteTrade(custId, tradeId);
    return tradeResponseDTO;
  }

  @GetMapping("v1")
  public Map<String, List<TradeHistory>> getAllTrades(@RequestHeader("customer_id") Long custId)
      throws ValidationException, InvalidActionException, DataNotFoundException {

    log.debug("Entered createNewTrade method for customer : {}", custId);
    Map<String, List<TradeHistory>> tradeHistoryDTOS = tradesService.getAllTrades(custId);
    return tradeHistoryDTOS;
  }
}
