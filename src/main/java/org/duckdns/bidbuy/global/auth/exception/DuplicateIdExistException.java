package org.duckdns.bidbuy.global.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateIdExistException extends RuntimeException {
  public DuplicateIdExistException(String message) {
    super(message);
  }
}
