package org.duckdns.bidbuy.app.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
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

    @Operation(summary = "내 정보 불러오기 API", description = "로그인된 사용자만 내 정보 불러올 수 있음")
    @GetMapping(value = "/me")
    public ResponseEntity<ApiResponse<?>> getUser() {
        MyProfileResponse responseDTO = userService.getMyProfile();
        ApiResponse<MyProfileResponse> response = new ApiResponse<>("200", "마이페이지 정보를 불러오는데 성공하였습니다.", responseDTO);
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

        // batch
        PageResponseDTO<List<MySalesResponse>> responseDTO = userService.getMySales(tradeStatus, pageable);
        ApiResponse<PageResponseDTO<List<MySalesResponse>>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

}
