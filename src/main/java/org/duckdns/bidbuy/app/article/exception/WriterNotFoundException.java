package org.duckdns.bidbuy.app.article.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class WriterNotFoundException extends RuntimeException{
    public WriterNotFoundException(Long id) {
        super("작성자를 찾을 수 없습니다: " + id);
    }
}
