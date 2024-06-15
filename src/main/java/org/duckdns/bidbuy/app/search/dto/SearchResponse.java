package org.duckdns.bidbuy.app.search.dto;

import lombok.Data;

import java.util.List;

public class SearchResponse {
    private Long totalCount;
    private List<SearchArticleResponse> searchResult;

    public SearchResponse(Long totalCount, List<SearchArticleResponse> searchResult) {
        this.totalCount = totalCount;
        this.searchResult = searchResult;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public List<SearchArticleResponse> getSearchResult() {
        return searchResult;
    }
}
