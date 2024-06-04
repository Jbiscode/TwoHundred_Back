package org.duckdns.bidbuy.app.article.dto;

import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.duckdns.bidbuy.app.article.domain.Article;

@Getter
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private Integer price;
    private Integer quantity;
    private String addr1;
    private String addr2;
    private Category category;
    private TradeMethod tradeMethod;
    private TradeStatus tradeStatus;

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getPrice(),
                article.getQuantity(),
                article.getAddr1(),
                article.getAddr2(),
                article.getCategory(),
                article.getTradeMethod(),
                article.getTradeStatus()
        );
    }
}
