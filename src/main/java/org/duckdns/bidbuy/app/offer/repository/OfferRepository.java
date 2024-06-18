package org.duckdns.bidbuy.app.offer.repository;

import org.duckdns.bidbuy.app.offer.domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query("SELECT COUNT(DISTINCT o.article.id) FROM Offer o WHERE o.offerer.id = :id")
    int countByOfferer_id(Long id);

    List<Offer> findByArticleId(Long articleId);

    @Query("""
            SELECT COUNT(o)
            FROM Offer o
            JOIN o.article a
            WHERE o.offerer.id = :userId
            AND o.isSelected = true
            AND a.tradeStatus = 'SOLD_OUT'
            """)
    int countBuy(@Param("userId") Long userId);

    Offer findByIsSelectedTrueAndOfferer_Id(Long userId);
}
