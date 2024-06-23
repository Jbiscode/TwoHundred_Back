package org.duckdns.bidbuy.app.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.duckdns.bidbuy.app.article.domain.*;
import org.duckdns.bidbuy.app.search.dto.LikeArticleResponse;
import org.duckdns.bidbuy.app.search.dto.SearchArticleResponse;
import org.duckdns.bidbuy.app.search.dto.SearchResponse;
import org.duckdns.bidbuy.app.search.dto.UserResponse;
import org.duckdns.bidbuy.app.search.service.SearchService;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search", produces = "application/json")
@Log4j2
public class SearchController {

    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<SearchResponse>> search(@RequestParam(required = false) String content,
                                                    @RequestParam(required = false) Category category,
                                                    @RequestParam(required = false) TradeMethod tradeMethod,
                                                    @RequestParam(required = false) TradeStatus tradeStatus,
                                                    @RequestParam(required = false) String orderBy,
                                                    @RequestParam int page,
                                                    @RequestParam int size) {

        //검색 게시글
        List<Article> search = searchService.search(category, tradeMethod, tradeStatus, content, orderBy, page, size);
        List<SearchArticleResponse> searchResult = search.stream()
                .map(s -> new SearchArticleResponse(s))
                .collect(toList());

        //게시글 총 수
        Long totalCount = searchService.totalCount(category, tradeMethod, tradeStatus, content);

        //검색된 게시글에서 좋아요
        List<LikeArticle> likeArticles = searchService.findLikeArticles(category, tradeMethod, content);
        List<LikeArticleResponse> likeArticleResult = likeArticles.stream()
                .map(l -> new LikeArticleResponse(l))
                .collect(toList());



        SearchResponse result = new SearchResponse(totalCount, searchResult, likeArticleResult);

        ApiResponse<SearchResponse> response = new ApiResponse<>("200", "검색결과 페이지 조회 완료", result);
        return ResponseEntity.ok(response);
    }

    //내 주변
    @GetMapping("/myLocation")
    public ResponseEntity<ApiResponse<SearchResponse>> searchLocation(@RequestParam(required = false) String content,
                                                              @RequestParam(required = false) Category category,
                                                              @RequestParam(required = false) TradeMethod tradeMethod,
                                                              @RequestParam(required = false) TradeStatus tradeStatus,
                                                              @RequestParam(required = false) String orderBy,
                                                              @RequestParam int page,
                                                              @RequestParam int size,
                                                              @RequestParam(required = false) Long id) {

        List<User> user = searchService.findUserAddress(id);
        List<UserResponse> address = user.stream()
                .map(u -> new UserResponse((u)))
                .collect(toList());

        //검색 게시글
        List<Article> search = searchService.searchLocation(category, tradeMethod, tradeStatus, orderBy, page, size, address);
        List<SearchArticleResponse> searchResult = search.stream()
                .map(s -> new SearchArticleResponse(s))
                .collect(toList());

        //게시글 총 수
        Long totalCount = searchService.myLocationtotalCount(category, tradeMethod, tradeStatus, address);

        //검색된 게시글에서 좋아요
        List<LikeArticle> likeArticles = searchService.findLikeArticles(category, tradeMethod, content);
        List<LikeArticleResponse> likeArticleResult = likeArticles.stream()
                .map(l -> new LikeArticleResponse(l))
                .collect(toList());



        SearchResponse result = new SearchResponse(totalCount, searchResult, likeArticleResult);

        ApiResponse<SearchResponse> response = new ApiResponse<>("200", "검색결과 페이지 조회 완료", result);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/")
//    public ResponseEntity<ApiResponse<List<UserResponse>>> findUser(@RequestParam(required = false) Long id) {
//        List<UserResponse> result = new ArrayList<>();
//
//        if (id != null) {
//            List<User> users = searchService.findUser(id);
//            result = users.stream()
//                    .map(u -> new UserResponse(u.getId(), u.getAddr1(), u.getAddr2()))
//                    .collect(toList());
//        }
//        ApiResponse<List<UserResponse>> response = new ApiResponse<>("200", "유저 정보 조회 완료", result);
//        return ResponseEntity.ok(response);
//    }

}