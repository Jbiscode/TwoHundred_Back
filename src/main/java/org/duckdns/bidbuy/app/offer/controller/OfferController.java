package org.duckdns.bidbuy.app.offer.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.offer.dto.OfferAcceptResponse;
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

    @Operation(summary = "게시글에 대한 가격 제안 API", description = "로그인한 사용자만 제안 가능")
    @PostMapping(value = "/{articleId}")
    public ResponseEntity<ApiResponse<OfferResponse>> createOffer(@PathVariable Long articleId, @RequestBody OfferRequest requestDTO) {
        OfferResponse responseDTO = offerService.createOffer(articleId, requestDTO);
        ApiResponse<OfferResponse> response = new ApiResponse<>("201", "가격 제안이 성공적으로 생성되었습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글에 대한 가격 제안 수락 API", description = "로그인한 사용자 중 해당 게시글 작성자만 제안 수락 가능")
    @PutMapping(value = "/{articleId}/{offerId}")
    public ResponseEntity<ApiResponse<OfferAcceptResponse>> acceptOffer(@PathVariable Long offerId, @PathVariable Long articleId) {
        OfferAcceptResponse responseDTO = offerService.acceptOffer(offerId, articleId);
        ApiResponse<OfferAcceptResponse> response = new ApiResponse<>("200", "가격 제안이 성공적으로 수락되었습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "제안 취소 API", description = "로그인한 사용자 중 제안 당사자만 취소 가능")
    @DeleteMapping(value = "/{offerId}")
    public ResponseEntity<ApiResponse<Void>> cancelOffer(@PathVariable Long offerId) {
        offerService.cancelOffer(offerId);
        ApiResponse<Void> response = new ApiResponse<>("200", "제안이 성공적으로 취소되었습니다.", null);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "수락한 제안 취소 API", description = "로그인한 사용자 중 해당 게시글 작성자만 취소 가능")
    @PutMapping(value = "/{articleId}/{offerId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelAcceptedOffer(@PathVariable Long offerId, @PathVariable Long articleId) {
        offerService.cancelAcceptedOffer(offerId, articleId);
        ApiResponse<Void> response = new ApiResponse<>("200", "수락한 제안이 성공적으로 취소되었습니다.", null);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "판매 완료 API", description = "로그인한 사용자 중 해당 게시글 작성자만 완료 가능")
    @PutMapping(value = "/{articleId}/complete")
    public ResponseEntity<ApiResponse<Void>> completeSale(@PathVariable Long articleId) {
        offerService.completeSale(articleId);
        ApiResponse<Void> response = new ApiResponse<>("200", "판매가 성공적으로 완료되었습니다.", null);
        return ResponseEntity.ok(response);
    }

}
