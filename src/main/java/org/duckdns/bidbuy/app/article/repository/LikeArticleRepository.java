package org.duckdns.bidbuy.app.article.repository;

import org.duckdns.bidbuy.app.article.domain.LikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {

    int countByUser_id(long id);
}
