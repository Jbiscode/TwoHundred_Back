package org.duckdns.bidbuy.app.search.dto;

import lombok.Data;
import lombok.Getter;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class SearchArticleResponse {
    private Long id;

    private Category category;

    private String title;
    private String content;
    private Integer price;
    private String addr1;
    private String addr2;
    private Long viewCount;

    private TradeMethod tradeMethod;

    private LocalDateTime createdDate;




    public SearchArticleResponse(Article article) {
        id = article.getId();
        category = article.getCategory();
        title = article.getTitle();
        content = article.getContent();
        price = article.getPrice();
        addr1 = article.getAddr1();
        addr2 = article.getAddr2();
        viewCount = article.getViewCount();
        tradeMethod = article.getTradeMethod();
        createdDate = article.getCreatedDate();
    }
}
