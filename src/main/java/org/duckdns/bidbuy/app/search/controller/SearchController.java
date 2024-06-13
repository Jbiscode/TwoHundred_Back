package org.duckdns.bidbuy.app.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;
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

    @GetMapping("")
    public ResponseEntity<ApiResponse<List>> search(@RequestParam(required = false) String content,
                                                    @RequestParam(required = false) Category category,
                                                    @RequestParam(required = false) TradeMethod tradeMethod,
                                                    @RequestParam(required = false) String orderBy) {
        List<Article> search = searchService.search(category, tradeMethod, content, orderBy);
        List<SearchArticleResponse> result = search.stream()
                .map(s -> new SearchArticleResponse(s))
                .collect(toList());
        ApiResponse<List> response = new ApiResponse<>("200", "검색결과 페이지: 내용&키테고리 조회 완료", result);
        return ResponseEntity.ok(response);
    }

}