package com.smallcase.stocktrade.service;

import com.smallcase.stocktrade.dto.TradeRequestDTO;
import com.smallcase.stocktrade.dto.TradeResponseDTO;
import com.smallcase.stocktrade.entities.TradeHistory;
import com.smallcase.stocktrade.exception.DataNotFoundException;
import com.smallcase.stocktrade.exception.InvalidActionException;
import com.smallcase.stocktrade.exception.ValidationException;
import java.util.List;
import java.util.Map;

/**
 * @author Meenal created on 08/11/21 at 9:21 PM
 **/
public interface TradesService {

  TradeResponseDTO createNewTrade(Long custId, TradeRequestDTO tradeRequestDTO)
      throws ValidationException, InvalidActionException;

  TradeResponseDTO updateTrade(Long custId, TradeRequestDTO tradeRequestDTO)
      throws ValidationException, InvalidActionException;

  Boolean deleteTrade(Long custId, Long tradeId) throws ValidationException, InvalidActionException;

  Map<String, List<TradeHistory>> getAllTrades(Long custId) throws DataNotFoundException;
}
