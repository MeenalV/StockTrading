package com.smallcase.stocktrade.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Meenal created on 15/03/22 at 12:00 AM
 **/

@Data
@Entity
@Table(name = "Portfolios")
public class Portfolio extends AuditFields {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="portfolio_seq_gen")
  @SequenceGenerator(name = "portfolio_seq_gen", sequenceName="portfolio_seq",allocationSize=1)
  private Long id;
  private Long customerId;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "portfolio_id")
  @Fetch(value = FetchMode.SUBSELECT)
  private List<Trade> trades;

  private Boolean active = Boolean.TRUE;
}
