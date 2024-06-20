package org.duckdns.bidbuy.app.review.service;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.article.repository.ArticleRepository;
import org.duckdns.bidbuy.app.offer.domain.Offer;
import org.duckdns.bidbuy.app.offer.exception.OfferNotFoundException;
import org.duckdns.bidbuy.app.offer.repository.OfferRepository;
import org.duckdns.bidbuy.app.review.domain.Review;
import org.duckdns.bidbuy.app.review.dto.ReviewRequest;
import org.duckdns.bidbuy.app.review.dto.ReviewResponse;
import org.duckdns.bidbuy.app.review.exception.ReviewDeleteFailException;
import org.duckdns.bidbuy.app.review.exception.ReviewDuplicateException;
import org.duckdns.bidbuy.app.review.exception.ReviewNotFoundException;
import org.duckdns.bidbuy.app.review.repository.ReviewRepository;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.dto.MySalesResponse;
import org.duckdns.bidbuy.app.user.dto.PageResponseDTO;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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
        Offer offer = offerRepository.findByIsSelectedTrueAndOfferer_Id(userId, articleId);
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

    public PageResponseDTO<List<ReviewResponse>> getReviews(Long userId, String status, Pageable pageable) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        Page<Object[]> reviews;
        if(status.equals("SALE")){
            reviews = reviewRepository.findByRevieweeId(userId, pageable);
        }else{
            reviews = reviewRepository.findByReviewerId(userId, pageable);
        }

        List<ReviewResponse> responses = reviews.stream()
                .map(this::createResponse)
                .toList();
        PageResponseDTO pageResponseDTO = new PageResponseDTO(responses, pageable, reviews.getTotalElements());
        return pageResponseDTO;

    }

    public ReviewResponse getReview(Long articleId) {
        Review review = reviewRepository.findByArticleId(articleId);

        return ReviewResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .articleTitle(review.getArticle().getTitle())
                .timeAgo(getTimeAgo(review.getCreatedDate()))
                .articleId(review.getArticle().getId())
                .reviewerName(review.getReviewer().getUsername())
                .reviewerLevel(review.getReviewer().getOfferLevel())
                .revieweeName(review.getReviewee().getUsername())
                .revieweeLevel(review.getReviewee().getOfferLevel())
                .score(review.getScore())
                .build();
    }

    @Transactional
    public String updateReview(Long articleId, ReviewRequest reviewRequest) {

        Review review = reviewRepository.findByArticleId(articleId);
        if(review == null) throw new ReviewNotFoundException("해당 리뷰가 존재하지 않습니다.");

        review.updateReview(reviewRequest);
        Review savedReview = reviewRepository.save(review);

        boolean isReviewUpdated = savedReview != null;
        return isReviewUpdated ? "리뷰가 수정되었습니다." : "리뷰 수정에 실패했습니다.";

    }

    private ReviewResponse createResponse(Object[] reviews) {
        Long reviewId = (Long) reviews[0];
        String content = (String) reviews[1];
        LocalDateTime createdDate = (LocalDateTime) reviews[2];
        Long articleId = (Long) reviews[3];
        String articleTitle = (String) reviews[4];
        String reviewerName = (String) reviews[5];
        Integer reviewerLevel = (Integer) reviews[6];
        String revieweeName = (String) reviews[7];
        Integer revieweeLevel = (Integer) reviews[8];
        Integer score = reviews.length > 9 ? (Integer) reviews[9] :  null;

        String timeAgo = getTimeAgo(createdDate);

        return new ReviewResponse(reviewId, articleId, articleTitle, reviewerName,revieweeName,reviewerLevel,revieweeLevel,content,timeAgo,score);
    }

    @Transactional
    public String deleteReview(Long articleId) {
        Review review = reviewRepository.findByArticleId(articleId);
        if(review == null) throw new ReviewNotFoundException("해당 리뷰가 존재하지 않습니다.");

        Long reviewId = review.getId();
        reviewRepository.delete(review);

        Review deletedReview = reviewRepository.findById(reviewId).orElse(null);
        if(deletedReview != null) {
            throw new ReviewDeleteFailException("리뷰 삭제에 실패했습니다.");
        }

        return "리뷰를 삭제했습니다.";

    }

    // 현재 시간과의 차이 계산
    private String getTimeAgo(LocalDateTime createdDate) {
        Duration duration = Duration.between(createdDate, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (seconds < 3600) {
            return (int) (seconds / 60) + "분 전";
        } else if (seconds < 86400) {
            return (int) (seconds / 3600) + "시간 전";
        } else {
            return (int) (seconds / 86400) + "일 전";
        }
    }



}
