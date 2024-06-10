package org.duckdns.bidbuy.app.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;
import org.duckdns.bidbuy.app.search.dto.SearchArticleRequest;
import org.duckdns.bidbuy.app.search.dto.SearchArticleResponse;
import org.duckdns.bidbuy.app.search.service.SearchService;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search", produces = "application/json")
@Log4j2
public class SearchController {

    private final SearchService searchService;

    //전체 조회
//    @GetMapping("")
//    public ResponseEntity<ApiResponse<List>> search() {
//        List<Article> search = searchService.searchAll();
//        List<SearchArticleResponse> result = search.stream()
//                .map(s -> new SearchArticleResponse(s))
//                .collect(toList());
//        ApiResponse<List> response = new ApiResponse<>("200", "검색결과 페이지: 게시글 전체조회 완료", result);
//        return ResponseEntity.ok(response);
//    }


    //카테고리&내용 조회
    @GetMapping("")
    public ResponseEntity<ApiResponse<List>> search(@RequestParam(required = false) String content,
                                                           @RequestParam(required = false) Category category,
                                                           @RequestParam(required = false) TradeMethod tradeMethod) {
        List<Article> search = searchService.search(category, tradeMethod, content);
        List<SearchArticleResponse> result = search.stream()
                .map(s -> new SearchArticleResponse(s))
                .collect(toList());
        ApiResponse<List> response = new ApiResponse<>("200", "검색결과 페이지: 내용&키테고리 조회 완료", result);
        return ResponseEntity.ok(response);
    }

    //낮은 가격순
    @GetMapping("/orderByRowPrice")
    public ResponseEntity<ApiResponse<List>> searchOrderByRowPrice(@RequestParam(required = false) String content,
                                                    @RequestParam(required = false) Category category,
                                                    @RequestParam(required = false) TradeMethod tradeMethod) {
        List<Article> search = searchService.searchOrderByRowPrice(category, tradeMethod, content);
        List<SearchArticleResponse> result = search.stream()
                .map(s -> new SearchArticleResponse(s))
                .collect(toList());
        ApiResponse<List> response = new ApiResponse<>("200", "검색결과 페이지: 낮은 가격순 조회 완료", result);
        return ResponseEntity.ok(response);
    }

    //높은 가격순
    @GetMapping("/orderByHighPrice")
    public ResponseEntity<ApiResponse<List>> searchOrderByHighPrice(@RequestParam(required = false) String content,
                                                                   @RequestParam(required = false) Category category,
                                                                   @RequestParam(required = false) TradeMethod tradeMethod) {
        List<Article> search = searchService.searchOrderByHighPrice(category, tradeMethod, content);
        List<SearchArticleResponse> result = search.stream()
                .map(s -> new SearchArticleResponse(s))
                .collect(toList());
        ApiResponse<List> response = new ApiResponse<>("200", "검색결과 페이지: 높은 가격순 조회 완료", result);
        return ResponseEntity.ok(response);
    }

    //최신순
    @GetMapping("/orderByLatest")
    public ResponseEntity<ApiResponse<List>> searchOrderByLatest(@RequestParam(required = false) String content,
                                                                    @RequestParam(required = false) Category category,
                                                                    @RequestParam(required = false) TradeMethod tradeMethod) {
        List<Article> search = searchService.searchOrderByLatest(category, tradeMethod, content);
        List<SearchArticleResponse> result = search.stream()
                .map(s -> new SearchArticleResponse(s))
                .collect(toList());
        ApiResponse<List> response = new ApiResponse<>("200", "검색결과 페이지: 최신순 조회 완료", result);
        return ResponseEntity.ok(response);
    }

    //인기순
    @GetMapping("/orderByHot")
    public ResponseEntity<ApiResponse<List>> searchOrderByHot(@RequestParam(required = false) String content,
                                                                 @RequestParam(required = false) Category category,
                                                                 @RequestParam(required = false) TradeMethod tradeMethod) {
        List<Article> search = searchService.searchOrderByHot(category, tradeMethod, content);
        List<SearchArticleResponse> result = search.stream()
                .map(s -> new SearchArticleResponse(s))
                .collect(toList());
        ApiResponse<List> response = new ApiResponse<>("200", "검색결과 페이지: 인기순 조회 완료", result);
        return ResponseEntity.ok(response);
    }

}