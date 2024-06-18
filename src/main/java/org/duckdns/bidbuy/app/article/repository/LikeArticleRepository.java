package org.duckdns.bidbuy.app.article.repository;

import org.duckdns.bidbuy.app.article.domain.LikeArticle;
import org.duckdns.bidbuy.app.offer.dto.OfferResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeArticleRepository extends JpaRepository<LikeArticle, Long> {

    int countByUser_id(long id);

    void deleteByArticleIdAndUserId(Long articleId, Long userId);

    Optional<LikeArticle> findByArticleIdAndUserId(Long articleId, Long userId);

    void deleteByArticleId(Long id);
}
