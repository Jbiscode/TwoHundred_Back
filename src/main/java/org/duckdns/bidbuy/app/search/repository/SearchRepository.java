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
import org.duckdns.bidbuy.app.article.dto.ArticleResponse;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRepository {

    private final EntityManager em;

    //전체 조회
    public List<Article> searchAll() {
        em.clear();
        return em.createQuery("select a from Article a", Article.class).getResultList();

    }


    //카테고리 & 내용 조회
    @Transactional(readOnly = true)
    public List<Article> search(Category category, TradeMethod tradeMethod, String content) {
        em.flush();
        em.clear();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        List<Predicate> predicates = new ArrayList<>();

        if (category != null) {
            predicates.add(cb.equal(article.get("category"), category));
        }

        if (tradeMethod != null) {
            predicates.add(cb.equal(article.get("tradeMethod"), tradeMethod));
        }

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

        TypedQuery<Article> query = em.createQuery(cq);
        return query.getResultList();
    }


    //낮은 가격순 정렬
    public List<Article> searchOrderByRowPrice(Category category, TradeMethod tradeMethod, String content) {
        em.flush();
        em.clear();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        List<Predicate> predicates = new ArrayList<>();

        if (category != null) {
            predicates.add(cb.equal(article.get("category"), category));
        }

        if (tradeMethod != null) {
            predicates.add(cb.equal(article.get("tradeMethod"), tradeMethod));
        }

        if (content != null && !content.isEmpty()) {
            String likePattern = "%" + content + "%";
            Predicate titlePredicate = cb.like(article.get("title"), likePattern);
            Predicate addr1Predicate = cb.like(article.get("addr1"), likePattern);
            Predicate addr2Predicate = cb.like(article.get("addr2"), likePattern);
            predicates.add(cb.or(titlePredicate, addr1Predicate, addr2Predicate));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        cq.orderBy(cb.asc(article.get("price")));

        TypedQuery<Article> query = em.createQuery(cq);
        return query.getResultList();
    }

    //높은 가격순 정렬
    public List<Article> searchOrderByHighPrice(Category category, TradeMethod tradeMethod, String content) {
        em.flush();
        em.clear();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        List<Predicate> predicates = new ArrayList<>();

        if (category != null) {
            predicates.add(cb.equal(article.get("category"), category));
        }

        if (tradeMethod != null) {
            predicates.add(cb.equal(article.get("tradeMethod"), tradeMethod));
        }

        if (content != null && !content.isEmpty()) {
            String likePattern = "%" + content + "%";
            Predicate titlePredicate = cb.like(article.get("title"), likePattern);
            Predicate addr1Predicate = cb.like(article.get("addr1"), likePattern);
            Predicate addr2Predicate = cb.like(article.get("addr2"), likePattern);
            predicates.add(cb.or(titlePredicate, addr1Predicate, addr2Predicate));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        cq.orderBy(cb.desc(article.get("price")));

        TypedQuery<Article> query = em.createQuery(cq);
        return query.getResultList();
    }

    //최신순 정렬
    public List<Article> searchOrderByLatest(Category category, TradeMethod tradeMethod, String content) {
        em.flush();
        em.clear();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        List<Predicate> predicates = new ArrayList<>();

        if (category != null) {
            predicates.add(cb.equal(article.get("category"), category));
        }

        if (tradeMethod != null) {
            predicates.add(cb.equal(article.get("tradeMethod"), tradeMethod));
        }

        if (content != null && !content.isEmpty()) {
            String likePattern = "%" + content + "%";
            Predicate titlePredicate = cb.like(article.get("title"), likePattern);
            Predicate addr1Predicate = cb.like(article.get("addr1"), likePattern);
            Predicate addr2Predicate = cb.like(article.get("addr2"), likePattern);
            predicates.add(cb.or(titlePredicate, addr1Predicate, addr2Predicate));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        cq.orderBy(cb.desc(article.get("createdDate")));

        TypedQuery<Article> query = em.createQuery(cq);
        return query.getResultList();
    }

    //인기순 정렬
    public List<Article> searchOrderByHot(Category category, TradeMethod tradeMethod, String content) {
        em.flush();
        em.clear();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Article> cq = cb.createQuery(Article.class);
        Root<Article> article = cq.from(Article.class);

        List<Predicate> predicates = new ArrayList<>();

        if (category != null) {
            predicates.add(cb.equal(article.get("category"), category));
        }

        if (tradeMethod != null) {
            predicates.add(cb.equal(article.get("tradeMethod"), tradeMethod));
        }

        if (content != null && !content.isEmpty()) {
            String likePattern = "%" + content + "%";
            Predicate titlePredicate = cb.like(article.get("title"), likePattern);
            Predicate addr1Predicate = cb.like(article.get("addr1"), likePattern);
            Predicate addr2Predicate = cb.like(article.get("addr2"), likePattern);
            predicates.add(cb.or(titlePredicate, addr1Predicate, addr2Predicate));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        cq.orderBy(cb.desc(article.get("viewCount")));

        TypedQuery<Article> query = em.createQuery(cq);
        return query.getResultList();
    }

}
