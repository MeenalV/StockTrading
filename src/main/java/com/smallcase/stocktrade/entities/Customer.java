package com.smallcase.stocktrade.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

/**
 * @author Meenal created on 15/03/22 at 12:07 AM
 **/

@Data
@Entity
@Table(name = "Customers")
public class Customer extends AuditFields {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="customers_seq_gen")
  @SequenceGenerator(name = "customers_seq_gen", sequenceName="customers_seq",allocationSize=1)
  private Long id;
  private String name;
  private Boolean active = Boolean.TRUE;
}
