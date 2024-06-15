package org.duckdns.bidbuy.app.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.review.domain.Review;
import org.duckdns.bidbuy.app.review.dto.ReviewRequest;
import org.duckdns.bidbuy.app.review.dto.ReviewResponse;
import org.duckdns.bidbuy.app.review.service.ReviewService;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/reviews", produces = "application/json", consumes = "application/json" )
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성하기 API", description = "내가 구매한 상품만 작성가능")
    @PostMapping(value = "/{articleId}")
    public ResponseEntity<ApiResponse<?>> createReview(
            @PathVariable Long articleId,
            @RequestBody ReviewRequest reviewRequest) {
        String message = reviewService.createReview(articleId,reviewRequest);


        return ResponseEntity.ok(new ApiResponse<>("201", message, null));
    }

    @Operation(summary = "리뷰 조회하기 API", description = "해당 게시글의 리뷰 조회")
    @GetMapping(value = "/{articleId}")
    public ResponseEntity<ApiResponse<?>> getReviews(@PathVariable Long articleId) {
         ReviewResponse reviewResponse = reviewService.getReview(articleId);

         return ResponseEntity.ok(new ApiResponse<>("200", "리뷰 내용을 불러오는데 성공했습니다.", reviewResponse));
    }

}
