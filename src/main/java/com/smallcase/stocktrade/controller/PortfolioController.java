package com.smallcase.stocktrade.controller;

import com.smallcase.stocktrade.dto.PortfolioTradesDTO;
import com.smallcase.stocktrade.dto.ReturnsDTO;
import com.smallcase.stocktrade.exception.DataNotFoundException;
import com.smallcase.stocktrade.exception.InvalidActionException;
import com.smallcase.stocktrade.exception.ValidationException;
import com.smallcase.stocktrade.service.PortfolioService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Meenal created on 15/03/22 at 12:28 AM
 **/

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/cloud/frontier/smallcase/portfolio")
public class PortfolioController {

  private final PortfolioService portfolioService;

  @GetMapping("v1")
  public List<PortfolioTradesDTO> getPortfolioTrades(@RequestHeader("customer_id") Long custId)
      throws DataNotFoundException, ValidationException {

    log.debug("Entered getPortfolioTrades method for customer : {}", custId);
    List<PortfolioTradesDTO> portfolioTradesDTOs = portfolioService.getPortfolioTrades(custId);
    return portfolioTradesDTOs;
  }

  @GetMapping("v1/returns")
  public ReturnsDTO getReturns(@RequestHeader("customer_id") Long custId)
      throws ValidationException, DataNotFoundException {

    log.debug("Entered createNewTrade method for customer : {}", custId);
    ReturnsDTO returnsDTO = portfolioService.getReturns(custId);
    return returnsDTO;
  }
}
