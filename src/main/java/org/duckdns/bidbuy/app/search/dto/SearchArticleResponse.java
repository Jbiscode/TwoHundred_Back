package org.duckdns.bidbuy.app.search.dto;

import lombok.Getter;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.ProductImage;
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
    private String thumbnailUrl;

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


        if (article.getProductImages() != null && !article.getProductImages().isEmpty()) {
            ProductImage firstImage = article.getProductImages().get(0);
            thumbnailUrl = firstImage.getThumbnailUrl();
            imageId = firstImage.getId();
        } else {
            thumbnailUrl = null;
            imageId = null;
        }

    }
}
