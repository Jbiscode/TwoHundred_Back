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

    public List<Article> search(Category category, TradeMethod tradeMethod, String content, String orderBy, int page, int size) {
        return searchRepository.search(category, tradeMethod, content, orderBy, page, size);
    }

    public Long totalCount(Category category, TradeMethod tradeMethod, String content) {
        return searchRepository.totalCount(category, tradeMethod, content);
    }
}
