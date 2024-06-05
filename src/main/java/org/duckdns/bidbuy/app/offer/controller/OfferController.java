package org.duckdns.bidbuy.app.offer.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.offer.dto.OfferRequest;
import org.duckdns.bidbuy.app.offer.dto.OfferResponse;
import org.duckdns.bidbuy.app.offer.service.OfferService;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/offers", produces = "application/json", consumes = "application/json")
public class OfferController {

    private final OfferService offerService;

    //게시글에 대한 가격 제안
    @Operation(summary = "게시글에 대한 가격 제안 API", description = "로그인한 사용자만 제안 가능")
    @PostMapping(value = "/{articleId}")
    public ResponseEntity<ApiResponse<OfferResponse>> createOffer(@PathVariable Long articleId, @RequestBody OfferRequest requestDTO) {
        OfferResponse responseDTO = offerService.createOffer(articleId, requestDTO);
        ApiResponse<OfferResponse> response = new ApiResponse<>("201", "가격 제안이 성공적으로 생성되었습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }


}
