package org.duckdns.bidbuy.app.search.dto;

import lombok.Getter;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;

import java.time.LocalDateTime;

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
    private Long imageId;
    private String imageUrl;

    private TradeMethod tradeMethod;

    private LocalDateTime createdDate;



    public SearchArticleResponse(Article article) {
        this.id = article.getId();
        this.category = article.getCategory();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.price = article.getPrice();
        this.addr1 = article.getAddr1();
        this.addr2 = article.getAddr2();
        this.viewCount = article.getViewCount();
        this.tradeMethod = article.getTradeMethod();
        this.createdDate = article.getCreatedDate();
        this.imageUrl = article.getProductImages().get(0).getImageUrl();
        this.imageId = article.getProductImages().get(0).getId();
    }
}
