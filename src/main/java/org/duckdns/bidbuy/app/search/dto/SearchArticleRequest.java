package org.duckdns.bidbuy.app.search.dto;

import lombok.Data;
import lombok.Getter;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;

@Data
@Getter
public class SearchArticleRequest {
    private Long id;

    private Category category;

    private String title;
    private String content;
    private Integer price;
    private Integer quantity;
    private Long likeCount;
    private Long viewCount;
    private String addr1;
    private String addr2;

    private TradeMethod tradeMethod;

}
