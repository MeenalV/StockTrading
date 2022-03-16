package com.smallcase.stocktrade.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author Meenal created on 15/03/22 at 12:00 AM
 **/
@Data
@Entity
@Table(name = "Trades")
public class Trade extends AuditFields {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="trade_seq_gen")
  @SequenceGenerator(name = "trade_seq_gen", sequenceName="trade_seq",allocationSize=1)
  private Long id;
  private String tickerSymbol;
  private Long shares = 0L;
  private Double avgSharePrice = 0d;

  private Long customerId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "portfolio_id", insertable = true, updatable = true)
  private Portfolio portfolio;

  private Boolean active = Boolean.TRUE;
}
