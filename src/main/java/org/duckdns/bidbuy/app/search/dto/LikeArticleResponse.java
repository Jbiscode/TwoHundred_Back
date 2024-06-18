package org.duckdns.bidbuy.app.search.dto;

import lombok.Getter;
import org.duckdns.bidbuy.app.article.domain.LikeArticle;

@Getter
public class LikeArticleResponse {
    private Long like_article_id;
    private Long user_id;
    private Long article_id;

    public LikeArticleResponse(LikeArticle likeArticle) {
        like_article_id = likeArticle.getId();
        user_id = likeArticle.getUser().getId();
        article_id = likeArticle.getArticle().getId();
    }
}
