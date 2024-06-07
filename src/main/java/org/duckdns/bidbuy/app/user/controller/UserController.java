package org.duckdns.bidbuy.app.user.controller;


import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.user.dto.MyProfileResponse;
import org.duckdns.bidbuy.app.user.dto.MySalesResponse;
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

    @GetMapping(value = "/me")
    public ResponseEntity<ApiResponse<?>> getUser() {
        MyProfileResponse responseDTO = userService.getMyProfile();
        ApiResponse<MyProfileResponse> response = new ApiResponse<>("200", "마이페이지 정보를 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/me/{tradeStatus}")
    public ResponseEntity<ApiResponse<Page<MySalesResponse>>> getUserSales(
            @PageableDefault( page=0, size = 4, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable(name = "tradeStatus") TradeStatus tradeStatus) {
        System.out.println("skf dho ahtqkedk!!!!!!");

        // batch
        Page<MySalesResponse> responseDTO = userService.getMySales(tradeStatus, pageable);
        ApiResponse<Page<MySalesResponse>> response = new ApiResponse<>("200", "판매중인 상품목록을 불러오는데 성공하였습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

}
