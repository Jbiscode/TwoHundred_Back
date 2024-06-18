package org.duckdns.bidbuy.app.article.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.offer.dto.OfferResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleDetailResponse {

    private Long id;
    private String title;
    private String content;
    private Integer price;
    private Integer quantity;
    private String addr1;
    private String addr2;
    private LocalDateTime createdDate;
    private Category category;
    private TradeMethod tradeMethod;
    private TradeStatus tradeStatus;
    private Long writerId;
    private String writerUsername;
    private String writerProfileImageUrl;
    private String thumbnailUrl;
    private String[] imageUrls;
    private boolean liked;
    private Long likeCount;

    private List<OfferResponse> offers;

}
