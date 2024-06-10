package org.duckdns.bidbuy.app.article.repository;

import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.offer.dto.OfferResponse;
import org.duckdns.bidbuy.app.user.dto.MySalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    int countByWriter_Id(Long userId);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null")
    Page<Object[]> findByWriterIdAndTradeStatus(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate, pi.thumbnailUrl " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN a.likes la " +
            "WHERE pi.thumbnailUrl IS NOT NULL " +
            "  AND la.user.id = :userId")
    Page<Object[]> findArticlesByUserIdWithLikes(@Param("userId") Long userId,Pageable pageable);


    @Query("""
        SELECT
            a.id,
            a.title,
            a.price,
            a.addr1,
            a.addr2,
            a.tradeStatus,
            a.createdDate,
            pi.thumbnailUrl,
            CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END AS isLiked
        FROM Article a
        JOIN a.productImages pi
        LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
        WHERE a.id IN (SELECT o.article.id FROM Offer o WHERE o.offerer.id = :userId)
            AND pi.thumbnailUrl IS NOT NULL
    """)
    Page<Object[]> getOfferedArticlesByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT
        a.id,
        a.title,
        a.price,
        a.addr1,
        a.addr2,
        a.tradeStatus,
        a.createdDate,
        pi.thumbnailUrl,
        CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END AS isLiked
    FROM Article a
    JOIN a.productImages pi
    LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
    WHERE a.id IN (
        SELECT o.article.id
        FROM Offer o
        WHERE o.offerer.id = :userId AND o.isSelected = TRUE
    )
    AND pi.thumbnailUrl IS NOT NULL
""")
    Page<Object[]> getOfferedArticlesByUserIdAndIsSelected(@Param("userId") Long userId, Pageable pageable);
}
