package org.duckdns.bidbuy.app.review.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.user.domain.UserEntity;
import org.duckdns.bidbuy.global.common.entity.BaseEntity;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Review extends BaseEntity {

    private String content;

    private boolean isRevieweeBuyer;

    private int score;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity reviewer;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    /* from_article 어떻게 해야할지 ? */
}
