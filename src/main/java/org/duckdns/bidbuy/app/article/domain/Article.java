package org.duckdns.bidbuy.app.article.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.duckdns.bidbuy.app.offer.domain.Offer;
import org.duckdns.bidbuy.app.review.domain.Review;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.global.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;


@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "article")
public class Article extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String title;
    private String content;
    private Integer price;
    private Integer quantity;
    private Long likeCount;
    private Long viewCount;


    @Enumerated(EnumType.STRING)
    private TradeMethod tradeMethod;

    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @OneToMany(mappedBy = "article")
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<Offer> offers = new ArrayList<>();

    @OneToOne(mappedBy = "article")
    private Review review;

    @OneToMany(mappedBy = "article")
    private List<LikeArticle> likes = new ArrayList<>();

}
