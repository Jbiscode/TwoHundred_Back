package org.duckdns.bidbuy.app.article.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.ProductImage;
import org.duckdns.bidbuy.app.article.dto.ArticleRequest;
import org.duckdns.bidbuy.app.article.dto.ArticleResponse;
import org.duckdns.bidbuy.app.article.repository.ArticleRepository;
import org.duckdns.bidbuy.app.article.repository.ProductImageRepository;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;
    private final ImageUploadService imageUploadService;

    @Transactional
    public ArticleResponse createArticle(ArticleRequest requestDTO, MultipartFile[] images) throws IOException {
        // 현재 로그인한 사용자 정보 가져오기 (예시로 ID가 1인 사용자라고 가정)
        User writer = userRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Article article = Article.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .price(requestDTO.getPrice())
                .quantity(requestDTO.getQuantity())
                .addr1(requestDTO.getAddr1())
                .addr2(requestDTO.getAddr2())
                .category(requestDTO.getCategory())
                .tradeMethod(requestDTO.getTradeMethod())
                .tradeStatus(requestDTO.getTradeStatus())
                .viewCount(0L)
                .likeCount(0L)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .writer(writer)
                .build();

        Article savedArticle = articleRepository.save(article);

        List<String> imageUrls = imageUploadService.uploadImages(images);
        for (String imageUrl : imageUrls) {
            ProductImage productImage = new ProductImage(null, imageUrl, savedArticle);
            productImageRepository.save(productImage);
        }

        return ArticleResponse.from(savedArticle);
    }


@Transactional
public ArticleResponse updateArticle(Long id, ArticleRequest requestDTO, MultipartFile[] images) throws IOException {
    Article article = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

    // 게시글 업데이트
    Article updatedArticle = Article.builder()
            .id(id)
            .title(requestDTO.getTitle() != null ? requestDTO.getTitle() : article.getTitle())
            .content(requestDTO.getContent() != null ? requestDTO.getContent() : article.getContent())
            .price(requestDTO.getPrice() != null ? requestDTO.getPrice() : article.getPrice())
            .quantity(requestDTO.getQuantity() != null ? requestDTO.getQuantity() : article.getQuantity())
            .addr1(requestDTO.getAddr1() != null ? requestDTO.getAddr1() : article.getAddr1())
            .addr2(requestDTO.getAddr2() != null ? requestDTO.getAddr2() : article.getAddr2())
            .category(requestDTO.getCategory() != null ? requestDTO.getCategory() : article.getCategory())
            .tradeMethod(requestDTO.getTradeMethod() != null ? requestDTO.getTradeMethod() : article.getTradeMethod())
            .tradeStatus(requestDTO.getTradeStatus() != null ? requestDTO.getTradeStatus() : article.getTradeStatus())
            .viewCount(article.getViewCount())
            .likeCount(article.getLikeCount())
            .createdDate(article.getCreatedDate())
            .modifiedDate(LocalDateTime.now())
            .writer(article.getWriter())
            .build();
    articleRepository.save(updatedArticle);

    // 이미지 변경이 있는 경우에만 기존 이미지 삭제 및 새 이미지 업로드
    if (images != null && images.length > 0) {
        List<ProductImage> existingImages = productImageRepository.findByArticle(article);
        for (ProductImage productImage : existingImages) {
            imageUploadService.deleteImage(productImage.getImageUrl());
        }
        productImageRepository.deleteAll(existingImages);

        List<String> imageUrls = imageUploadService.uploadImages(images);
        for (String imageUrl : imageUrls) {
            ProductImage productImage = new ProductImage(null, imageUrl, updatedArticle);
            productImageRepository.save(productImage);
        }
    }

    return ArticleResponse.from(updatedArticle);
}

    //    @Transactional
//    public void deleteArticle(Long id) {
//        Article article = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
//        productImageRepository.deleteByArticle(article);
//        articleRepository.delete(article);
//    }
    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        List<ProductImage> images = productImageRepository.findByArticle(article);
        for (ProductImage productImage : images) {
            imageUploadService.deleteImage(productImage.getImageUrl());  // Object Storage에서 이미지 삭제
        }
        productImageRepository.deleteByArticle(article);  // DB에서 이미지 레코드 삭제
        articleRepository.delete(article);  // 게시글 삭제
    }
}