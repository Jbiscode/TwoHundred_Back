package org.duckdns.bidbuy.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NullInputException extends RuntimeException{

    public NullInputException(String message) {
        super(message);
    }
}
