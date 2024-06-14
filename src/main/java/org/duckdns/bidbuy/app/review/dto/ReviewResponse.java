package org.duckdns.bidbuy.app.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long reviewId; // review
    private Long articleId; // review

    private String articleTitle; // article
    private String reviewerName; // review
    private String revieweeName; // review
    private Integer reviewerLevel; // user
    private Integer revieweeLevel; // user

    private String content; // review
    private String timeAgo; // review
}
