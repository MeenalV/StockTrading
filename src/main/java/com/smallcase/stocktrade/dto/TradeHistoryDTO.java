package com.smallcase.stocktrade.dto;

import com.smallcase.stocktrade.entities.TradeHistory;
import java.util.List;
import lombok.Data;

/**
 * @author Meenal created on 16/03/22 at 3:14 PM
 **/

@Data
public class TradeHistoryDTO {

  private String tickerSymbol;
  private List<TradeHistory> trades;

}
