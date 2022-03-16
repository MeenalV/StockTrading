package com.smallcase.stocktrade.dto;

import lombok.Data;

/**
 * @author Meenal created on 15/03/22 at 12:34 AM
 **/
@Data
public class PortfolioTradesDTO {
  private String tickerSymbol;
  private Long shares;
  private Double avgSharePrice;
  private Double investedAmount;
}
