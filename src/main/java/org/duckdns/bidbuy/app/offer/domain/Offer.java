package org.duckdns.bidbuy.app.offer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.domain.UserEntity;
import org.duckdns.bidbuy.global.common.entity.BaseEntity;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name ="offer")
public class Offer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Long id;

    private boolean isSelected;

    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offerer_id")
    private User offerer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
}
