package org.duckdns.bidbuy.app.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyBuysResponse {
    private Long id;
    private String title;
    private Integer price;
    private String addr1;
    private String addr2;
    private TradeStatus tradeStatus;
    private String timeAgo;
    private String thumbnailUrl;
    private Boolean isLiked;
    private Boolean isReviewed;
}
