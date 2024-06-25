package org.duckdns.bidbuy.app.user.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LENGTH_REQUIRED)
public class PasswordLengthException extends RuntimeException{

    public PasswordLengthException(String message) {
        super(message);
    }
}
