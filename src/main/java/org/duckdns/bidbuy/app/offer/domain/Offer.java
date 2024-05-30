package org.duckdns.bidbuy.app.offer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Offer extends BaseEntity {

    private boolean isSelected;

    private int price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
}
