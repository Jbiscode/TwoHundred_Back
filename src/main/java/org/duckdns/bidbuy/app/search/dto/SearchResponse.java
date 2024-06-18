package org.duckdns.bidbuy.app.search.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class SearchResponse {
    private Long totalCount;
    private List<SearchArticleResponse> searchResult;
    private List<LikeArticleResponse> likeResult;

    public SearchResponse(Long totalCount, List<SearchArticleResponse> searchResult, List<LikeArticleResponse> likeResult) {
        this.totalCount = totalCount;
        this.searchResult = searchResult;
        this.likeResult = likeResult;
    }

}
