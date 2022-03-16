package com.smallcase.stocktrade.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author Meenal created on 15/03/22 at 12:43 AM
 **/

@Data
@Entity
@Table(name = "Trades_History")
public class TradeHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="trade_audit_seq_gen")
  @SequenceGenerator(name = "trade_audit_seq_gen", sequenceName="trade_audit_seq",allocationSize=1)
  private Long id;
  private String tickerSymbol;
  private String tradeType;
  private Long shares;
  private Double perSharePrice;
  private Long customerId;
  private Long portfolioId;
  private Long tradeId;
  private Boolean active = Boolean.TRUE;
  private String action;
}
