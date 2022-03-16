package com.smallcase.stocktrade.dto;

import java.util.List;
import lombok.Data;

/**
 * @author Meenal created on 16/03/22 at 2:58 PM
 **/
@Data
public class ReturnsDTO {
  private List<PortfolioTradesDTO> portfolioTradesDTOS;
  private Double returns;
}
