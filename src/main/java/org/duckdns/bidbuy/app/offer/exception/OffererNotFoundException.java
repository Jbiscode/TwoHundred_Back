package org.duckdns.bidbuy.app.offer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OffererNotFoundException extends RuntimeException{
    public OffererNotFoundException(Long id) {
        super("제안자를 찾을 수 없습니다 " + id);
    }
}
