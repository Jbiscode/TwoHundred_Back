package org.duckdns.bidbuy.app.article.repository;

import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.user.dto.MySalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a " +
            "JOIN a.offers o " +
            "WHERE o.offerer.id = :userId " +
            "AND a.tradeStatus = 'SOLD_OUT'")
    List<Article> findSoldOutArticlesByUserId(@Param("userId") Long userId);

    int countByWriter_Id(Long userId);

    @Query("SELECT a.id, a.title, a.price, a.addr1, a.addr2, a.tradeStatus, a.createdDate ,pi.thumbnailUrl " +
            "FROM Article a " +
            "LEFT JOIN a.productImages pi " +
            "WHERE a.writer.id = :userId AND a.tradeStatus = :tradeStatus AND pi.thumbnailUrl is not null")
    Page<Object[]> findByWriterIdAndTradeStatus(@Param("userId") Long userId, @Param("tradeStatus") TradeStatus tradeStatus, Pageable pageable);

}
