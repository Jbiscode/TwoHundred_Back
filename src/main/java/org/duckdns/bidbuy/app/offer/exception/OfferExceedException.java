package org.duckdns.bidbuy.app.offer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OfferExceedException extends RuntimeException {
    public OfferExceedException(Integer price) {
        super(price + "원 보다 높은 가격으로 제안할 수 없습니다.");
    }
}
