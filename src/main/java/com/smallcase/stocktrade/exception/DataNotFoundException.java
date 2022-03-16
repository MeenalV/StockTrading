package com.smallcase.stocktrade.exception;

import lombok.Data;

/**
 * @author Meenal created on 15/03/22 at 12:55 AM
 **/
@Data
public class DataNotFoundException extends Exception {


  private int code;
  public DataNotFoundException(final String message, int code) {
    super(message);
    this.code = code;
  }
}
