package org.duckdns.bidbuy.app.article.repository;

import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    void deleteByArticle(Article article);
    List<ProductImage> findByArticle(Article article);
    ProductImage findByImageUrl(String imageUrl);
}
