package org.duckdns.bidbuy.app.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.review.dto.ReviewResponse;
import org.duckdns.bidbuy.app.review.service.ReviewService;
import org.duckdns.bidbuy.app.user.dto.MyInfoResponse;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = "application/json", consumes = "application/json")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;

    @Operation(summary = "내 프로필 불러오기 API", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me")
    public ResponseEntity<ApiResponse<?>> getMe() {
        MyProfileResponse responseDTO = userService.getMyProfile();
        ApiResponse<MyProfileResponse> response = new ApiResponse<>("200", "마이페이지 정보를 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 불러오기 API")
    @GetMapping(value = "/me/info")
    public ResponseEntity<ApiResponse<?>> getMeInfo() {
        MyInfoResponse responseDTO = userService.getMyInfo();
        ApiResponse<MyInfoResponse> response = new ApiResponse<>("200", "내 정보를 불러오는데 성공했습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 수정하기 API")
    @PutMapping(value = "/me/info",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<?>> updateMeInfo(
            @RequestPart(value = "myInfoResponseDTO") MyInfoResponse myInfoResponseDTO,
            @RequestPart(value = "img", required = false) List<MultipartFile> list
    ) throws IOException {
        String result = userService.updateUserInfo(myInfoResponseDTO, list);
        ApiResponse<MyInfoResponse> response = new ApiResponse<>("200", result, null);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 판매상품 정보 불러오기 API", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/sales")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserSales(
            @PageableDefault( page=0, size = 4) Pageable pageable,
            @RequestParam(name = "tradeStatus") TradeStatus tradeStatus,
            @RequestParam(name = "sorting", required = false, defaultValue = "latest") String sorting) {

        // batch
        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMySales(sorting,tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 찜한상품 목록 불러오기 API", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/likes")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserLikeArticles(
            @RequestParam(name = "sorting", required = false, defaultValue = "latest") String sorting,
            @PageableDefault(page = 0, size = 4) Pageable pageable) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getLikeArticles(sorting, pageable);

        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 가격제안 정보 불러오기 API", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/offers")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserOffers(
            @PageableDefault( page=0, size = 4) Pageable pageable,
            @RequestParam(name = "tradeStatus") TradeStatus tradeStatus,
            @RequestParam(name = "sorting", required = false, defaultValue = "latest") String sorting) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMyOffers(tradeStatus, sorting,pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "가격제안한 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 구매내역 정보 불러오기 API", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me/buys")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserBuys(
            @PageableDefault( page=0, size = 4) Pageable pageable,
            @RequestParam(name = "tradeStatus") TradeStatus tradeStatus,
            @RequestParam(name = "sorting", required = false, defaultValue = "latest") String sorting) {

        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMyBuys(tradeStatus, sorting,pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "구매내역을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 찜하기 API", description = "로그인된 사용자만 찜할 수 있음 / 찜하기 및 찜삭제")
    @PutMapping(value = "/me/likes/{id}")
    public ResponseEntity<ApiResponse<?>> UpdateUserLikeArticles(@PathVariable(name = "id") Long articleId) {

        String messages = userService.updateLikeArticles(articleId);
        ApiResponse response = new ApiResponse("200", messages, null);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 정보 불러오기 API", description = "다른 유저의 프로필 정보 불러오기")
    @GetMapping(value = "/{userId}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable(name = "userId") Long userId) {
        MyProfileResponse responseDTO = userService.getUserProfile(userId);
        ApiResponse<MyProfileResponse> response = new ApiResponse<>("200", "마이페이지 정보를 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "다른 사용자 판매상품 정보 불러오기 API", description = "다른 사용자의 판매상품 정보 불러오기")
    @GetMapping(value = "/{userId}/sales")
    public ResponseEntity<ApiResponse<PageResponseDTO<List<MySalesResponse>>>> getUserSales(
            @PathVariable(name = "userId") Long userId,
            @PageableDefault( page=0, size = 4) Pageable pageable,
            @RequestParam(name = "tradeStatus") TradeStatus tradeStatus,
            @RequestParam(name = "sorting", required = false, defaultValue = "latest") String sorting) {
//        log.error("다른사용자판매상품 컨트롤러1");
        // batch
        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getUserSales(userId,tradeStatus, sorting,pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
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
