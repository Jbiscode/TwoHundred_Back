package org.duckdns.bidbuy.app.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.search.dto.SearchArticleDTO;
import org.duckdns.bidbuy.app.search.service.SearchService;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search", produces = "application/json")
@Log4j2
public class SearchController {

    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List>> search() {
        List<Article> search = searchService.searchAll();
        List<SearchArticleDTO> result = search.stream()
                .map(s -> new SearchArticleDTO(s))
                .collect(toList());
        ApiResponse<List> response = new ApiResponse<>("200", "검색결과 페이지", result);
        return ResponseEntity.ok(response);
    }

}