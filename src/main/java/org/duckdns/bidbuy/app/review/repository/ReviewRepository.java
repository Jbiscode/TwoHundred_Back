package org.duckdns.bidbuy.app.review.repository;

import org.duckdns.bidbuy.app.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"article"})
    Review findByArticleId(Long articleId);

    @Query("SELECT r.id, r.content, r.createdDate, a.id, a.title, u1.username AS reviewerName, u1.offerLevel AS reviewerLevel, u2.name AS revieweeName, u2.offerLevel AS revieweeLevel, r.score " +
            "FROM Review r " +
            "JOIN r.article a " +
            "JOIN r.reviewer u1 " +
            "JOIN r.reviewee u2 " +
            "WHERE r.reviewee.id = :revieweeId " +
            "ORDER BY r.createdDate DESC")
    Page<Object[]> findByRevieweeId(@Param("revieweeId") Long revieweeId, Pageable pageable);

    @Query("SELECT r.id, r.content, r.createdDate, a.id, a.title, u1.username AS reviewerName, u1.offerLevel AS reviewerLevel, u2.name AS revieweeName, u2.offerLevel AS revieweeLevel, r.score " +
            "FROM Review r " +
            "JOIN r.article a " +
            "JOIN r.reviewer u1 " +
            "JOIN r.reviewee u2 " +
            "WHERE r.reviewer.id = :reviewerId " +
            "ORDER BY r.createdDate DESC")
    Page<Object[]> findByReviewerId(@Param("reviewerId") Long reviewerId, Pageable pageable);

    int countByRevieweeIdOrReviewerId(Long userId, Long userId1);
}
