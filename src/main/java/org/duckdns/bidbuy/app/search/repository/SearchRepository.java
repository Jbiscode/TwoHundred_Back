package org.duckdns.bidbuy.app.search.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.dto.ArticleResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRepository {

    private final EntityManager em;

    public List<Article> searchAll() {
        return em.createQuery("select a from Article a", Article.class).getResultList();
    }
}
