package org.duckdns.bidbuy.app.article.repository;

import org.duckdns.bidbuy.app.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
