package com.smallcase.stocktrade.entities;

import java.util.Date;
import lombok.Data;

/**
 * @author Meenal created on 15/03/22 at 12:13 AM
 **/

@Data
public class AuditFields {

  private String createdBy;
  private String modifiedBy;
  private Date createdAt;
  private Date modifiedAt;
}
