package org.duckdns.bidbuy.app.search.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;
import org.duckdns.bidbuy.app.article.dto.ArticleResponse;
import org.duckdns.bidbuy.app.search.repository.SearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    public List<Article> searchAll() {
        return searchRepository.searchAll();
    }

    //카테고리&내용 조회
    public List<Article> search(Category category, TradeMethod tradeMethod, String content) {
        return searchRepository.search(category, tradeMethod, content);
    }

    public List<Article> searchOrderByRowPrice(Category category, TradeMethod tradeMethod, String content) {
        return searchRepository.searchOrderByRowPrice(category, tradeMethod, content);
    }

    public List<Article> searchOrderByHighPrice(Category category, TradeMethod tradeMethod, String content) {
        return searchRepository.searchOrderByHighPrice(category, tradeMethod, content);
    }

    public List<Article> searchOrderByLatest(Category category, TradeMethod tradeMethod, String content) {
        return searchRepository.searchOrderByLatest(category, tradeMethod, content);
    }

    public List<Article> searchOrderByHot(Category category, TradeMethod tradeMethod, String content) {
        return searchRepository.searchOrderByHot(category, tradeMethod, content);
    }
}
