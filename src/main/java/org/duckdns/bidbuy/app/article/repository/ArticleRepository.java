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
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.createdDate DESC")
    Page<Object[]> findByWriterIdAndTradeStatus(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price DESC")
    Page<Object[]> findByWriterIdAndTradeStatusOrderByPriceDESC(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price ASC")
    Page<Object[]> findByWriterIdAndTradeStatusOrderByPriceASC(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, FALSE AS isLiked " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.createdDate DESC")
    Page<Object[]> findByWriterIdAndTradeStatusUser(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, FALSE AS isLiked " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price DESC")
    Page<Object[]> findByWriterIdAndTradeStatusUserOrderByPriceDESC(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, FALSE AS isLiked " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price ASC")
    Page<Object[]> findByWriterIdAndTradeStatusUserOrderByPriceASC(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END AS isLiked " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :loggedInUserId " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.createdDate DESC")
    Page<Object[]> findByWriterIdAndTradeStatusUserLoggedIn(@Param("userId") Long userId, @Param("loggedInUserId") Long loggedInUserId,@Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END AS isLiked " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :loggedInUserId " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price DESC")
    Page<Object[]> findByWriterIdAndTradeStatusUserLoggedInOrderByPriceDESC(@Param("userId") Long userId, @Param("loggedInUserId") Long loggedInUserId,@Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END AS isLiked " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :loggedInUserId " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price ASC")
    Page<Object[]> findByWriterIdAndTradeStatusUserLoggedInOrderByPriceASC(@Param("userId") Long userId, @Param("loggedInUserId") Long loggedInUserId,@Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate, pi.thumbnailUrl " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN a.likes la " +
            "WHERE pi.thumbnailUrl IS NOT NULL " +
            "  AND la.user.id = :userId " +
            "ORDER BY a.createdDate DESC")
    Page<Object[]> findArticlesByUserIdWithLikes(@Param("userId") Long userId,Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate, pi.thumbnailUrl " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN a.likes la " +
            "WHERE pi.thumbnailUrl IS NOT NULL " +
            "  AND la.user.id = :userId " +
            "ORDER BY a.price DESC")
    Page<Object[]> findArticlesByUserIdWithLikesOrderByPriceDesc(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate, pi.thumbnailUrl " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN a.likes la " +
            "WHERE pi.thumbnailUrl IS NOT NULL " +
            "  AND la.user.id = :userId " +
            "ORDER BY a.price ASC")
    Page<Object[]> findArticlesByUserIdWithLikesOrderByPriceAsc(@Param("userId") Long userId, Pageable pageable);


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
        ORDER BY a.createdDate DESC
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
        WHERE a.id IN (SELECT o.article.id FROM Offer o WHERE o.offerer.id = :userId)
            AND pi.thumbnailUrl IS NOT NULL
        ORDER BY a.price DESC
    """)
    Page<Object[]> getOfferedArticlesByUserIdOrderByPriceDESC(@Param("userId") Long userId, Pageable pageable);

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
        ORDER BY a.price ASC
    """)
    Page<Object[]> getOfferedArticlesByUserIdOrderByPriceASC(@Param("userId") Long userId, Pageable pageable);

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
        CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END AS isLiked,
        CASE WHEN r.id IS NOT NULL THEN TRUE ELSE FALSE END AS isReviewed
    FROM Article a
    JOIN a.productImages pi
    LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
    LEFT JOIN Review r ON r.id = a.review.id
    WHERE a.id IN (
        SELECT o.article.id
        FROM Offer o
        WHERE o.offerer.id = :userId AND o.isSelected = TRUE
    )
    AND pi.thumbnailUrl IS NOT NULL
    ORDER BY a.createdDate DESC
""")
    Page<Object[]> getOfferedArticlesByUserIdAndIsSelected(@Param("userId") Long userId, Pageable pageable);

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
        CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END AS isLiked,
        CASE WHEN r.id IS NOT NULL THEN TRUE ELSE FALSE END AS isReviewed
    FROM Article a
    JOIN a.productImages pi
    LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
    LEFT JOIN Review r ON r.id = a.review.id
    WHERE a.id IN (
        SELECT o.article.id
        FROM Offer o
        WHERE o.offerer.id = :userId AND o.isSelected = TRUE
    )
    AND pi.thumbnailUrl IS NOT NULL
    ORDER BY a.price DESC
""")
    Page<Object[]> getOfferedArticlesByUserIdAndIsSelectedOrderByPriceDESC(@Param("userId") Long userId, Pageable pageable);

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
        CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END AS isLiked,
        CASE WHEN r.id IS NOT NULL THEN TRUE ELSE FALSE END AS isReviewed
    FROM Article a
    JOIN a.productImages pi
    LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
    LEFT JOIN Review r ON r.id = a.review.id
    WHERE a.id IN (
        SELECT o.article.id
        FROM Offer o
        WHERE o.offerer.id = :userId AND o.isSelected = TRUE
    )
    AND pi.thumbnailUrl IS NOT NULL
    ORDER BY a.price ASC
""")
    Page<Object[]> getOfferedArticlesByUserIdAndIsSelectedOrderByPriceASC(@Param("userId") Long userId, Pageable pageable);
}
