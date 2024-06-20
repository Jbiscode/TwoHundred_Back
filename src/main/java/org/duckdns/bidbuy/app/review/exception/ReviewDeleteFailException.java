package org.duckdns.bidbuy.app.review.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReviewDeleteFailException extends RuntimeException {
    public ReviewDeleteFailException(String message) {
        super(message);
    }
}
