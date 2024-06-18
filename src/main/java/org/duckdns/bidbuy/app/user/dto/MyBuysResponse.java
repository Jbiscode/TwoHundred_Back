package org.duckdns.bidbuy.app.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;

import java.time.Duration;
import java.time.LocalDateTime;

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
    private LocalDateTime createdDate;
    private String timeAgo;
    private String thumbnailUrl;
    private Boolean isLiked;
    private Boolean isReviewed;

    public MyBuysResponse(Long id, String title, Integer price, String addr1, String addr2, TradeStatus tradeStatus, LocalDateTime createdDate, String thumbnailUrl, Boolean isLiked, Boolean isReviewed) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.tradeStatus = tradeStatus;
        this.createdDate = createdDate;
        this.timeAgo = getTimeAgo(createdDate);
        this.thumbnailUrl = thumbnailUrl;
        this.isLiked = isLiked;
        this.isReviewed = isReviewed;
    }

    private String getTimeAgo(LocalDateTime createdDate) {
        Duration duration = Duration.between(createdDate, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (seconds < 3600) {
            return (int) (seconds / 60) + "분 전";
        } else if (seconds < 86400) {
            return (int) (seconds / 3600) + "시간 전";
        } else {
            return (int) (seconds / 86400) + "일 전";
        }
    }
}
