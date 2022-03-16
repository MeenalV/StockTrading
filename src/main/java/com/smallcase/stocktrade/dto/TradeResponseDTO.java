package com.smallcase.stocktrade.dto;

import lombok.Data;

/**
 * @author Meenal created on 15/03/22 at 12:22 AM
 **/
@Data
public class TradeResponseDTO extends TradeRequestDTO{

  private Double oldAvgPrice;
  private Double newAvgPrice;
  private Long oldSharesCount;
  private Long newSharesCount;

}
