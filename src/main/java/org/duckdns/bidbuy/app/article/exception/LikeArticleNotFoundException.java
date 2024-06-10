package org.duckdns.bidbuy.app.article.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LikeArticleNotFoundException extends RuntimeException {

    public LikeArticleNotFoundException(String message) {
        super(message);
    }
}
