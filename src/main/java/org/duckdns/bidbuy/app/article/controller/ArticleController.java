// ArticleController.java
package org.duckdns.bidbuy.app.article.controller;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.duckdns.bidbuy.app.article.dto.ArticleRequest;
import org.duckdns.bidbuy.app.article.dto.ArticleResponse;
import org.duckdns.bidbuy.app.article.service.ArticleService;
import org.duckdns.bidbuy.app.article.service.ImageUploadService;
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
@RequestMapping(value = "/api/v1/article", produces = "application/json")
@Log4j2
public class ArticleController {

    private final ArticleService articleService;

    private final ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadImage(@RequestParam("images") MultipartFile[] multipartFiles) throws IOException {
        List<String> imageUrls= imageUploadService.uploadImages(multipartFiles);
        return ResponseEntity.ok(imageUrls);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ArticleResponse>> createArticle(@ModelAttribute ArticleRequest requestDTO) throws IOException {
        ArticleResponse responseDTO = articleService.createArticle(requestDTO, requestDTO.getFiles().toArray(new MultipartFile[0]));
        ApiResponse<ArticleResponse> response = new ApiResponse<>("201", "게시글이 성공적으로 생성되었습니다.", responseDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<ArticleResponse>> updateArticle(@PathVariable Long id, @ModelAttribute ArticleRequest requestDTO) throws IOException {
        ArticleResponse responseDTO = articleService.updateArticle(id, requestDTO, requestDTO.getFiles().toArray(new MultipartFile[0]));
        ApiResponse<ArticleResponse> response = new ApiResponse<>("200", "게시글이 성공적으로 수정되었습니다.", responseDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        ApiResponse<Void> response = new ApiResponse<>("200", "게시글이 성공적으로 삭제되었습니다.", null);
        return ResponseEntity.ok(response);
    }
}