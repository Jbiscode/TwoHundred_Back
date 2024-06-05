package org.duckdns.bidbuy.app.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleSummaryResponse {

    private Long id;
    private String title;
    private Category category;
    private Integer price;
    private TradeMethod tradeMethod;
    private TradeStatus tradeStatus;
    private LocalDateTime createdDate;
    private Long writerId;

}
