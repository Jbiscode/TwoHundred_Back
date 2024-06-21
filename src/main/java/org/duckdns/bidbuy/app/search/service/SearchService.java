package org.duckdns.bidbuy.app.search.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.*;
import org.duckdns.bidbuy.app.article.dto.ArticleResponse;
import org.duckdns.bidbuy.app.search.dto.UserResponse;
import org.duckdns.bidbuy.app.search.repository.SearchRepository;
import org.duckdns.bidbuy.app.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    public List<Article> search(Category category, TradeMethod tradeMethod, TradeStatus tradeStatus, String content, String orderBy, int page, int size) {
        return searchRepository.search(category, tradeMethod, tradeStatus, content, orderBy, page, size);
    }

    public Long totalCount(Category category, TradeMethod tradeMethod, TradeStatus tradeStatus, String content) {
        return searchRepository.totalCount(category, tradeMethod, tradeStatus, content);
    }

    public List<LikeArticle> findLikeArticles(Category category, TradeMethod tradeMethod, String content) {
        return searchRepository.findLikeArticles(category, tradeMethod, content);
    }

    public List<User> findUserAddress(Long id) {
        return searchRepository.findUserAddress(id);
    }

    public List<Article> searchLocation(Category category, TradeMethod tradeMethod, TradeStatus tradeStatus, String orderBy, int page, int size, List<UserResponse> address) {
        return searchRepository.searchLocation(category, tradeMethod, tradeStatus, orderBy, page, size, address);
    }

    public Long myLocationtotalCount(Category category, TradeMethod tradeMethod, TradeStatus tradeStatus, List<UserResponse> address) {
        return searchRepository.myLocationtotalCount(category, tradeMethod, tradeStatus, address);
    }
}
