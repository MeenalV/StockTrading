package com.smallcase.stocktrade.exception.handler;

import com.smallcase.stocktrade.dto.WSError;
import com.smallcase.stocktrade.exception.DataNotFoundException;
import com.smallcase.stocktrade.exception.InvalidActionException;
import com.smallcase.stocktrade.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class StockTradeControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {ValidationException.class})
  public ResponseEntity<Object> handleValidationException(ValidationException ex) {
    log.debug("Bad request exception");
    WSError wsError = new WSError();
    wsError.setCode(ex.getCode());
    wsError.setMessage(ex.getMessage());
    return ResponseEntity.badRequest().body(wsError);
  }

  @ExceptionHandler(value = {DataNotFoundException.class})
  public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException ex) {
    log.debug("Bad request exception");
    WSError wsError = new WSError();
    wsError.setMessage(ex.getMessage());
    wsError.setCode(ex.getCode());
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(value = {InvalidActionException.class})
  public ResponseEntity<Object> handleInvalidActionException(InvalidActionException ex) {
      log.debug("Bad request exception");
    WSError wsError = new WSError();
    wsError.setCode(ex.getCode());
    wsError.setMessage(ex.getMessage());
    return ResponseEntity.badRequest().body(wsError);
  }


}
