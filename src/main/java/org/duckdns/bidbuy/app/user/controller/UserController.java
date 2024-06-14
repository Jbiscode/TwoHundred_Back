package org.duckdns.bidbuy.app.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.review.dto.ReviewResponse;
import org.duckdns.bidbuy.app.review.service.ReviewService;
import org.duckdns.bidbuy.app.user.dto.MyProfileResponse;
import org.duckdns.bidbuy.app.user.dto.MySalesResponse;
import org.duckdns.bidbuy.app.user.dto.PageResponseDTO;
import org.duckdns.bidbuy.app.user.service.UserService;

import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = "application/json", consumes = "application/json")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;

    @Operation(summary = "내 정보 불러오기 API", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me")
    public ResponseEntity<ApiResponse<?>> getMe() {
        MyProfileResponse responseDTO = userService.getMyProfile();
        ApiResponse<MyProfileResponse> response = new ApiResponse<>("200", "마이페이지 정보를 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 정보 불러오기 API", description = "다른 유저의 프로필 정보 불러오기")
    @GetMapping(value = "/{userId}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable(name = "userId") Long userId) {
        MyProfileResponse responseDTO = userService.getUserProfile(userId);
        ApiResponse<MyProfileResponse> response = new ApiResponse<>("200", "마이페이지 정보를 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "다른 사용자 판매상품 정보 불러오기 API, 최신순 정렬", description = "다른 사용자의 판매상품 정보 불러오기")
    @GetMapping(value = "/{userId}/{tradeStatus}/latest")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserSalesOrderByLatest(
            @PathVariable(name = "userId") Long userId,
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "createdDate") Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {
        log.error("다른사용자판매상품 컨트롤러1");
        // batch
        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getUserSales(userId,tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "내 판매상품 정보 불러오기 API, 최신순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/{tradeStatus}/latest")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserSalesOrderByLatest(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "createdDate") Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {

        // batch
        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMySales(tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 판매상품 정보 불러오기 API, 낮은 가격순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/{tradeStatus}/low-price")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserSalesOrderByPriceASC(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.ASC, sort = "price") Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {

        // batch
        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMySales(tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 판매상품 정보 불러오기 API, 높은 가격순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/{tradeStatus}/high-price")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserSalesOrderByPriceDESC(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "price") Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMySales(tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 찜한상품 목록 불러오기 API, 최신순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/likes/latest")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserLikeArticlesOrderByLatest(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "createdDate") Pageable pageable) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getLikeArticles(pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 찜한상품 목록 불러오기 API, 높은 가격순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/likes/high-price")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserLikeArticlesOrderByPriceDESC(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "price") Pageable pageable) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getLikeArticles(pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 찜한상품 목록 불러오기 API, 낮은 가격순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/likes/low-price")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserLikeArticlesOrderByPriceASC(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.ASC, sort = "price") Pageable pageable) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getLikeArticles(pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 가격제안 정보 불러오기 API, 최신순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/offers/{tradeStatus}/latest")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserOffersOrderByLatest(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "createdDate") Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMyOffers(tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "가격제안한 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 가격제안 정보 불러오기 API, 높은 가격순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/offers/{tradeStatus}/high-price")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserOffersOrderByPriceDESC(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "createdDate") Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMyOffers(tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "가격제안한 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 가격제안 정보 불러오기 API, 높은 낮은순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/offers/{tradeStatus}/low-price")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserOffersOrderByPriceASC(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "createdDate") Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMyOffers(tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "가격제안한 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 찜하기 API", description = "로그인된 사용자만 찜할 수 있음 / 찜하기 및 찜삭제")
    @PutMapping(value = "/me/likes/{id}")
    public ResponseEntity<ApiResponse<?>> UpdateUserLikeArticles(@PathVariable(name = "id") Long articleId) {

        String messages = userService.updateLikeArticles(articleId);
        ApiResponse response = new ApiResponse("200", messages, null);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 구매내역 정보 불러오기 API, 최신순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/buys/{tradeStatus}/latest")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserBuysOrderByLatest(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "createdDate") Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMyBuys(tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "구매내역을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 구매내역 정보 불러오기 API, 높은 가격순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/buys/{tradeStatus}/high-price")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserBuysOrderByPriceDESC(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "price") Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMyBuys(tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "구매내역을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 구매내역 정보 불러오기 API, 낮은 가격순 정렬", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/buys/{tradeStatus}/low-price")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserBuysOrderByPriceASC(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "price") Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMyBuys(tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "구매내역을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "리뷰 목록 조회 API", description = "다른 사람의 프로필페이지에서 리뷰 목록 조회")
    @GetMapping(value = "/{userId}/reviews")
    public ResponseEntity<ApiResponse<?>> getUserReviews(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "status") String status,
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC, sort = "createdDate") Pageable pageable) {

        PageResponseDTO<List<ReviewResponse>> responseDTO = reviewService.getReviews(userId, status, pageable);
        ApiResponse<PageResponseDTO<List<ReviewResponse>>> response = new ApiResponse<>("200", "리뷰내역을 불러오는데 성공했습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }


}
