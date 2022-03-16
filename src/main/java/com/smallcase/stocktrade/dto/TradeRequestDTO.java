package com.smallcase.stocktrade.dto;

import lombok.Data;

/**
 * @author Meenal created on 15/03/22 at 12:17 AM
 **/

@Data
public class TradeRequestDTO {

  private Long id;
  private String tickerSymbol;
  private TradeType tradeType;
  private Long shares;
  private Double sharePrice;
}
