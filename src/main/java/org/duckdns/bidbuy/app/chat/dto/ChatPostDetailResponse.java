package org.duckdns.bidbuy.app.chat.dto;

import lombok.Builder;
import lombok.Getter;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;

@Builder
@Getter
public class ChatPostDetailResponse {
    private Long articleId;

    private String title;
    private String content;
    private String ProductImageUrl;
    private TradeStatus tradeStatus;
    private Long writerId;
    private String writerName;
    private String writerProfileImageUrl;
    private Long userId;
    private String offerProfileImageUrl;
}
