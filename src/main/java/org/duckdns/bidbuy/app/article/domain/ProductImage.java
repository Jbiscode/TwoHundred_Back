package org.duckdns.bidbuy.app.article.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.global.common.entity.BaseEntity;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "product_image")
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long id;

    private String imageUrl;
    private String thumbnailUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
}



