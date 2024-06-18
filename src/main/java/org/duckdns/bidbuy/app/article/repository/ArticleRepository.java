package org.duckdns.bidbuy.app.article.repository;

import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.offer.dto.OfferResponse;
import org.duckdns.bidbuy.app.user.dto.MyBuysResponse;
import org.duckdns.bidbuy.app.user.dto.MySalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    int countByWriter_Id(Long userId);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus ,a.createdDate,pi.thumbnailUrl) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.createdDate DESC")
    Page<MySalesResponse> findByWriterIdAndTradeStatus(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus ,a.createdDate,pi.thumbnailUrl) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price DESC")
    Page<MySalesResponse> findByWriterIdAndTradeStatusOrderByPriceDESC(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus ,a.createdDate,pi.thumbnailUrl) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price ASC")
    Page<MySalesResponse> findByWriterIdAndTradeStatusOrderByPriceASC(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, FALSE) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.createdDate DESC")
    Page<MySalesResponse> findByWriterIdAndTradeStatusUser(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, FALSE) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price DESC")
    Page<MySalesResponse> findByWriterIdAndTradeStatusUserOrderByPriceDESC(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, FALSE) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price ASC")
    Page<MySalesResponse> findByWriterIdAndTradeStatusUserOrderByPriceASC(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :loggedInUserId " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.createdDate DESC")
    Page<MySalesResponse> findByWriterIdAndTradeStatusUserLoggedIn(@Param("userId") Long userId, @Param("loggedInUserId") Long loggedInUserId,@Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :loggedInUserId " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price DESC")
    Page<MySalesResponse> findByWriterIdAndTradeStatusUserLoggedInOrderByPriceDESC(@Param("userId") Long userId, @Param("loggedInUserId") Long loggedInUserId,@Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl, CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :loggedInUserId " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null " +
            "ORDER BY a.price ASC")
    Page<MySalesResponse> findByWriterIdAndTradeStatusUserLoggedInOrderByPriceASC(@Param("userId") Long userId, @Param("loggedInUserId") Long loggedInUserId,@Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate, pi.thumbnailUrl) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN a.likes la " +
            "WHERE pi.thumbnailUrl IS NOT NULL " +
            "  AND la.user.id = :userId " +
            "ORDER BY a.createdDate DESC")
    Page<MySalesResponse> findArticlesByUserIdWithLikes(@Param("userId") Long userId,Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate, pi.thumbnailUrl) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN a.likes la " +
            "WHERE pi.thumbnailUrl IS NOT NULL " +
            "  AND la.user.id = :userId " +
            "ORDER BY a.price DESC")
    Page<MySalesResponse> findArticlesByUserIdWithLikesOrderByPriceDesc(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate, pi.thumbnailUrl) " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "LEFT JOIN a.likes la " +
            "WHERE pi.thumbnailUrl IS NOT NULL " +
            "  AND la.user.id = :userId " +
            "ORDER BY a.price ASC")
    Page<MySalesResponse> findArticlesByUserIdWithLikesOrderByPriceAsc(@Param("userId") Long userId, Pageable pageable);


    @Query("""
        SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(
            a.id,
            a.title,
            a.price,
            a.addr1,
            a.addr2,
            a.tradeStatus,
            a.createdDate,
            pi.thumbnailUrl,
            CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END)
        FROM Article a
        JOIN a.productImages pi
        LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
        WHERE a.id IN (SELECT o.article.id FROM Offer o WHERE o.offerer.id = :userId)
            AND pi.thumbnailUrl IS NOT NULL
        ORDER BY a.createdDate DESC
    """)
    Page<MySalesResponse> getOfferedArticlesByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(
            a.id,
            a.title,
            a.price,
            a.addr1,
            a.addr2,
            a.tradeStatus,
            a.createdDate,
            pi.thumbnailUrl,
            CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END)
        FROM Article a
        JOIN a.productImages pi
        LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
        WHERE a.id IN (SELECT o.article.id FROM Offer o WHERE o.offerer.id = :userId)
            AND pi.thumbnailUrl IS NOT NULL
        ORDER BY a.price DESC
    """)
    Page<MySalesResponse> getOfferedArticlesByUserIdOrderByPriceDESC(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(
            a.id,
            a.title,
            a.price,
            a.addr1,
            a.addr2,
            a.tradeStatus,
            a.createdDate,
            pi.thumbnailUrl,
            CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END)
        FROM Article a
        JOIN a.productImages pi
        LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
        WHERE a.id IN (SELECT o.article.id FROM Offer o WHERE o.offerer.id = :userId)
            AND pi.thumbnailUrl IS NOT NULL
        ORDER BY a.price ASC
    """)
    Page<MySalesResponse> getOfferedArticlesByUserIdOrderByPriceASC(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(
        a.id,
        a.title,
        a.price,
        a.addr1,
        a.addr2,
        a.tradeStatus,
        a.createdDate,
        pi.thumbnailUrl,
        CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END,
        CASE WHEN r.id IS NOT NULL THEN TRUE ELSE FALSE END)
    FROM Article a
    JOIN a.productImages pi
    LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
    LEFT JOIN Review r ON r.article.id = a.id
       WHERE a.id IN (
            SELECT DISTINCT o.article.id
            FROM Offer o
            WHERE o.offerer.id = :userId
        )
    AND pi.thumbnailUrl IS NOT NULL
     AND a.tradeStatus = 'SOLD_OUT'
    ORDER BY a.createdDate DESC
""")
    Page<MySalesResponse> getOfferedArticlesByUserIdAndIsSelected(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(
        a.id,
        a.title,
        a.price,
        a.addr1,
        a.addr2,
        a.tradeStatus,
        a.createdDate,
        pi.thumbnailUrl,
        CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END,
        CASE WHEN r.id IS NOT NULL THEN TRUE ELSE FALSE END)
    FROM Article a
    JOIN a.productImages pi
    LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
    LEFT JOIN Review r ON r.article.id = a.id
    WHERE a.id IN (
        SELECT o.article.id
        FROM Offer o
        WHERE o.offerer.id = :userId AND o.isSelected = TRUE
    )
    AND pi.thumbnailUrl IS NOT NULL
    AND a.tradeStatus = 'SOLD_OUT'
    ORDER BY a.price DESC
""")
    Page<MySalesResponse> getOfferedArticlesByUserIdAndIsSelectedOrderByPriceDESC(@Param("userId") Long userId, Pageable pageable);

    @Query("""
    SELECT new org.duckdns.bidbuy.app.user.dto.MySalesResponse(
        a.id,
        a.title,
        a.price,
        a.addr1,
        a.addr2,
        a.tradeStatus,
        a.createdDate,
        pi.thumbnailUrl,
        CASE WHEN la.id IS NOT NULL THEN TRUE ELSE FALSE END,
        CASE WHEN r.id IS NOT NULL THEN TRUE ELSE FALSE END)
    FROM Article a
    JOIN a.productImages pi
    LEFT JOIN LikeArticle la ON a.id = la.article.id AND la.user.id = :userId
    LEFT JOIN Review r ON r.article.id = a.id
    WHERE a.id IN (
        SELECT o.article.id
        FROM Offer o
        WHERE o.offerer.id = :userId AND o.isSelected = TRUE
    )
    AND pi.thumbnailUrl IS NOT NULL
    AND a.tradeStatus = 'SOLD_OUT'
    ORDER BY a.price ASC
""")
    Page<MySalesResponse> getOfferedArticlesByUserIdAndIsSelectedOrderByPriceASC(@Param("userId") Long userId, Pageable pageable);
}
