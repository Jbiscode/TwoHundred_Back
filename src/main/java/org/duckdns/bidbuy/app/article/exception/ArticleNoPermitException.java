package org.duckdns.bidbuy.app.article.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ArticleNoPermitException extends RuntimeException{
    public ArticleNoPermitException(Long id) {
        super("게시글에 대한 권한이 없습니다: " + id);
    }
}
