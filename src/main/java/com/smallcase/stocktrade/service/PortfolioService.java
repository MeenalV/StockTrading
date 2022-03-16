package com.smallcase.stocktrade.service;

import com.smallcase.stocktrade.dto.PortfolioTradesDTO;
import com.smallcase.stocktrade.dto.ReturnsDTO;
import com.smallcase.stocktrade.exception.DataNotFoundException;
import com.smallcase.stocktrade.exception.ValidationException;
import java.util.List;

/**
 * @author Meenal created on 08/11/21 at 9:21 PM
 **/
public interface PortfolioService {

  List<PortfolioTradesDTO> getPortfolioTrades(Long custId)
      throws ValidationException, DataNotFoundException;

  ReturnsDTO getReturns(Long custId) throws DataNotFoundException, ValidationException;
}
