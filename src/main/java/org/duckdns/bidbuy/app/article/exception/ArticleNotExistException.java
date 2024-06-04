package org.duckdns.bidbuy.app.article.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArticleNotExistException extends RuntimeException{
    public ArticleNotExistException(Long id) {
        super("게시글 데이터가 존재하지 않습니다: " + id);
    }
}
