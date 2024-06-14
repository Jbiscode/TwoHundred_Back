package org.duckdns.bidbuy.app.search.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRepository {

    private final EntityManager em;

    @Transactional(readOnly = true)
    public List<Article> search(Category category, TradeMethod tradeMethod, String content, String orderBy, int page, int size) {
        em.flush();
        em.clear();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        List<Predicate> predicates = new ArrayList<>();

        //카테고리
        if (category != null) {
            predicates.add(cb.equal(article.get("category"), category));
        }

        //거래 방식
        if (tradeMethod != null) {
            predicates.add(cb.equal(article.get("tradeMethod"), tradeMethod));
        }

        // 검색 내용
        if (content != null && !content.trim().isEmpty()) {
            String contentPattern = "%" + content + "%";
            Predicate contentPredicate = cb.or(
                    cb.like(article.get("title"), contentPattern),
                    cb.like(article.get("addr1"), contentPattern),
                    cb.like(article.get("addr2"), contentPattern)
            );
            predicates.add(contentPredicate);
        }

        cq.where(predicates.toArray(new Predicate[0]));

        //정렬
        if (orderBy != null && orderBy.equals("latest")){
            cq.orderBy(cb.desc(article.get("createdDate")));
        } else if (orderBy != null && orderBy.equals("lowPrice")){
            cq.orderBy(cb.asc(article.get("price")));
        } else if (orderBy != null && orderBy.equals("highPrice")){
            cq.orderBy(cb.desc(article.get("price")));
        }

        TypedQuery<Article> query = em.createQuery(cq);

        // 페이징
        int startIndex = (page - 1) * size;
        query.setFirstResult(startIndex);
        query.setMaxResults(size);


        return query.getResultList();
    }

}
