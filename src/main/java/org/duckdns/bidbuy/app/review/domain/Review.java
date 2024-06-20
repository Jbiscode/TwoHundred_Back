package org.duckdns.bidbuy.app.review.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.review.dto.ReviewRequest;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.global.common.entity.BaseEntity;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String content;

    private boolean isRevieweeBuyer;

    private Integer score;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    public void updateReview(ReviewRequest reviewRequest) {
        this.content = reviewRequest.getReviewContent();
        this.score = reviewRequest.getScore();
    }
}
