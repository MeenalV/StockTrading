package com.smallcase.stocktrade.dto;

import lombok.Data;

/**
 * @author Meenal created on 15/03/22 at 12:57 AM
 **/
@Data
public class WSError {

  private int code;
  private String message;
}
