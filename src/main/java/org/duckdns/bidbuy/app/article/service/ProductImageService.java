package org.duckdns.bidbuy.app.article.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.ProductImage;
import org.duckdns.bidbuy.app.article.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    @Transactional
    public ProductImage saveProductImage(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    @Transactional
    public void deleteProductImagesByArticle(Article article) {
        productImageRepository.deleteByArticle(article);
    }

    @Transactional(readOnly = true)
    public List<ProductImage> getProductImagesByArticle(Article article) {
        return productImageRepository.findByArticle(article);
    }

}
