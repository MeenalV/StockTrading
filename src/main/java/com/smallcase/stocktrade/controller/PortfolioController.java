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


/** This controller covers all the APIs for a portfolio .
 * My portfolio consists of various securities that we have and a portfolio can only have a return, and hence the return API
 * is a part of portfolio.
 *
 * Creating different controller for Portfolio to make sure only portfolio related data is transmitted from here
 * */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/cloud/frontier/smallcase/portfolio")
public class PortfolioController {

  private final PortfolioService portfolioService;

  /** A single user has a single trading portfolio and hence we use customer Id to get it's portfolio data. */
  @GetMapping("v1")
  public List<PortfolioTradesDTO> getPortfolioTrades(@RequestHeader("customer_id") Long custId)
      throws DataNotFoundException, ValidationException {

    log.debug("Entered getPortfolioTrades method for customer : {}", custId);
    List<PortfolioTradesDTO> portfolioTradesDTOs = portfolioService.getPortfolioTrades(custId);
    return portfolioTradesDTOs;
  }

  /** A single user has a single trading portfolio and hence the returns. So we use customer Id to get it's return */
  @GetMapping("v1/returns")
  public ReturnsDTO getReturns(@RequestHeader("customer_id") Long custId)
      throws ValidationException, DataNotFoundException {

    log.debug("Entered createNewTrade method for customer : {}", custId);
    ReturnsDTO returnsDTO = portfolioService.getReturns(custId);
    return returnsDTO;
  }
}
