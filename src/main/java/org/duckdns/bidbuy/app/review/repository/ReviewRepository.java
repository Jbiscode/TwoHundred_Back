package org.duckdns.bidbuy.app.review.repository;

import org.duckdns.bidbuy.app.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByArticleId(Long articleId);
}
