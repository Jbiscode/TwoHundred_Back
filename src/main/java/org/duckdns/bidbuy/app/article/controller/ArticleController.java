// ArticleController.java
package org.duckdns.bidbuy.app.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.duckdns.bidbuy.app.article.dto.ArticleRequest;
import org.duckdns.bidbuy.app.article.dto.ArticleResponse;
import org.duckdns.bidbuy.app.article.dto.ArticleDetailResponse;
import org.duckdns.bidbuy.app.article.dto.ArticleSummaryResponse;
import org.duckdns.bidbuy.app.article.service.ArticleService;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/articles", produces = "application/json")
@Log4j2
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "게시글 작성 API", description = "로그인된 사용자만 작성가능, 이미지 최소 1개 필수")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(@ModelAttribute ArticleRequest requestDTO) throws IOException {
        ArticleResponse responseDTO = articleService.createArticle(requestDTO, requestDTO.getFiles().toArray(new MultipartFile[0]));
        ApiResponse<ArticleResponse> response = new ApiResponse<>("201", "게시글이 성공적으로 생성되었습니다.", responseDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{articleId}")
                .buildAndExpand(responseDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "게시글 수정 API", description = "로그인한 사용자 중 해당 게시글 작성자만 수정 가능, 이미지 최소 1개 필수")
    @PutMapping(value = "/{articleId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ArticleResponse>> updateArticle(@PathVariable Long articleId, @ModelAttribute ArticleRequest requestDTO) throws IOException {
        ArticleResponse responseDTO = articleService.updateArticle(articleId, requestDTO, requestDTO.getFiles().toArray(new MultipartFile[0]));
        ApiResponse<ArticleResponse> response = new ApiResponse<>("200", "게시글이 성공적으로 수정되었습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 삭제 API", description = "로그인한 유저 정보를 검증하고 해당 게시글 작성자와 같을 경우 삭제 가능")
    @DeleteMapping(value = "/{articleId}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        ApiResponse<Void> response = new ApiResponse<>("200", "게시글이 성공적으로 삭제되었습니다.", null);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 조회 API", description = "게시글 상세정보 조회")
    @GetMapping(value = "/{articleId}")
    public ResponseEntity<ApiResponse<ArticleDetailResponse>> getArticleDetail(@PathVariable Long articleId){
        ArticleDetailResponse articleDetailResponse = articleService.getArticleDetail(articleId);
        ApiResponse<ArticleDetailResponse> response = new ApiResponse<>("200", "게시글 상세정보 조회 성공", articleDetailResponse);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 게시글 조회 API", description = "전체 게시글 정보 조회")
    @GetMapping(value = "/")
    public ResponseEntity<ApiResponse<List<ArticleSummaryResponse>>> getAllArticles(){
        List<ArticleSummaryResponse> articleSummaryResponses = articleService.getAllArticles();
        ApiResponse<List<ArticleSummaryResponse>> response = new ApiResponse<>("200", "전체 게시글 조회 성공", articleSummaryResponses);
        return ResponseEntity.ok(response);
    }
}