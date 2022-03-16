package com.smallcase.stocktrade.exception;

import lombok.Data;

/**
 * @author Meenal created on 15/03/22 at 12:55 AM
 **/
@Data
public class ValidationException extends Exception {


  private int code;
  public ValidationException(final String message, int code) {
    super(message);
    this.code = code;
  }
}
