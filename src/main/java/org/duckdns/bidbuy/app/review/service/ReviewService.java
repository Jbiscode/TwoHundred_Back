package org.duckdns.bidbuy.app.review.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.repository.ArticleRepository;
import org.duckdns.bidbuy.app.offer.domain.Offer;
import org.duckdns.bidbuy.app.offer.exception.OfferNotFoundException;
import org.duckdns.bidbuy.app.offer.repository.OfferRepository;
import org.duckdns.bidbuy.app.review.domain.Review;
import org.duckdns.bidbuy.app.review.dto.ReviewRequest;
import org.duckdns.bidbuy.app.review.exception.ReviewDuplicateException;
import org.duckdns.bidbuy.app.review.repository.ReviewRepository;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final OfferRepository offerRepository;

    @Transactional
    public String createReview(Long articleId, ReviewRequest reviewRequest) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();

        // offer에서 isSelected = true, offerer_id가 userId와 일치하지 않은경우 => 예외
        Offer offer = offerRepository.findByIsSelectedTrueAndOfferer_Id(userId);
        if(offer == null) throw new OfferNotFoundException("제안이 없거나 제안수락되지 않았습니다.");

        // 이미 리뷰가 있는경우 예외
        Review existReview = reviewRepository.findByArticleId(articleId);
        if(existReview != null) throw new ReviewDuplicateException("이미 작성된 리뷰가 존재합니다.");

        User reviewer = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        User reviewee = userRepository.findByArticlesId(articleId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));


        Review review = Review.builder()
                .content(reviewRequest.getReviewContent())
                .reviewer(reviewer)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .score(reviewRequest.getScore())
                .reviewee(reviewee)
                .article(article)
                .build();

        Review savedReview =  reviewRepository.save(review);

        boolean isReviewCreated = savedReview != null;
        String message = isReviewCreated ? "리뷰 작성에 성공했습니다." : "리뷰 작성에 실패했습니다.";

        return message;
    }
}
