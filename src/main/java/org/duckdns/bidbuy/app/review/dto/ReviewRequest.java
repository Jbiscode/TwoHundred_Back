package org.duckdns.bidbuy.app.review.dto;

import lombok.*;
import org.duckdns.bidbuy.app.review.domain.ReviewGrade;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewRequest {
    private Long articleId;
    private String reviewContent;
    private String reviewGrade;
    private Integer score;

    public void setReviewGrade(String reviewGrade) {
        this.reviewGrade = reviewGrade;
        this.score = convertReviewGradeToScore(reviewGrade);
    }

    private Integer convertReviewGradeToScore(String reviewGrade) {
        switch (reviewGrade) {
            case "good":
                return 1;
            case "soso":
                return 0;
            case "bad":
                return -1;
            default:
                return 0;
        }
    }

    // Getter, Setter, 생성자 등 필요한 메서드 추가
}
