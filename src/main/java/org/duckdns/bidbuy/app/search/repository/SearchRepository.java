package org.duckdns.bidbuy.app.search.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.*;
import org.duckdns.bidbuy.app.search.dto.UserResponse;
import org.duckdns.bidbuy.app.user.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRepository {

    private final EntityManager em;

    @Transactional(readOnly = true)
    public List<Article> search(Category category, TradeMethod tradeMethod, TradeStatus tradeStatus, String content, String orderBy, int page, int size) {
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

        //거래 상태
        if (tradeStatus != null) {
            predicates.add(cb.equal(article.get("tradeStatus"), tradeStatus));
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
        } else if (orderBy != null && orderBy.equals("hot")){
        cq.orderBy(cb.desc(article.get("viewCount")));
    }

        TypedQuery<Article> query = em.createQuery(cq);

        // 페이징
        int startIndex = (page - 1) * size;
        query.setFirstResult(startIndex);
        query.setMaxResults(size);


        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public long totalCount(Category category, TradeMethod tradeMethod, TradeStatus tradeStatus, String content) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Article> article = cq.from(Article.class);

        List<Predicate> predicates = new ArrayList<>();

        // 카테고리
        if (category != null) {
            predicates.add(cb.equal(article.get("category"), category));
        }

        // 거래 방식
        if (tradeMethod != null) {
            predicates.add(cb.equal(article.get("tradeMethod"), tradeMethod));
        }

        //거래 상태
        if (tradeStatus != null) {
            predicates.add(cb.equal(article.get("tradeStatus"), tradeStatus));
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

        cq.select(cb.count(article)).where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult();
    }

    public List<User> findUserAddress(Long id) {
        List<User> addresses = em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", id)
                .getResultList();

        return addresses;
    }

    public List<LikeArticle> findLikeArticles(Category category, TradeMethod tradeMethod, String content) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<LikeArticle> cq = cb.createQuery(LikeArticle.class);
        Root<LikeArticle> likeArticle = cq.from(LikeArticle.class);
        Join<LikeArticle, Article> article = likeArticle.join("article");

        List<Predicate> predicates = new ArrayList<>();

        // 카테고리
        if (category != null) {
            predicates.add(cb.equal(article.get("category"), category));
        }

        // 거래 방식
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

        TypedQuery<LikeArticle> query = em.createQuery(cq);

        return query.getResultList();
    }

    public List<Article> searchLocation(Category category, TradeMethod tradeMethod, TradeStatus tradeStatus, String orderBy, int page, int size, List<UserResponse> address) {
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

        //거래 상태
        if (tradeStatus != null) {
            predicates.add(cb.equal(article.get("tradeStatus"), tradeStatus));
        }

        // 주소
        if (address != null) {
            predicates.add(cb.equal(article.get("addr1"), address.get(0).getAddr1()));
            predicates.add(cb.equal(article.get("addr2"), address.get(0).getAddr2()));
        }


        cq.where(predicates.toArray(new Predicate[0]));

        //정렬
        if (orderBy != null && orderBy.equals("latest")){
            cq.orderBy(cb.desc(article.get("createdDate")));
        } else if (orderBy != null && orderBy.equals("lowPrice")){
            cq.orderBy(cb.asc(article.get("price")));
        } else if (orderBy != null && orderBy.equals("highPrice")){
            cq.orderBy(cb.desc(article.get("price")));
        } else if (orderBy != null && orderBy.equals("hot")){
            cq.orderBy(cb.desc(article.get("viewCount")));
        }

        TypedQuery<Article> query = em.createQuery(cq);

        // 페이징
        int startIndex = (page - 1) * size;
        query.setFirstResult(startIndex);
        query.setMaxResults(size);


        return query.getResultList();

    }

    public Long myLocationtotalCount(Category category, TradeMethod tradeMethod, TradeStatus tradeStatus, List<UserResponse> address) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Article> article = cq.from(Article.class);

        List<Predicate> predicates = new ArrayList<>();

        // 카테고리
        if (category != null) {
            predicates.add(cb.equal(article.get("category"), category));
        }

        // 거래 방식
        if (tradeMethod != null) {
            predicates.add(cb.equal(article.get("tradeMethod"), tradeMethod));
        }

        //거래 상태
        if (tradeStatus != null) {
            predicates.add(cb.equal(article.get("tradeStatus"), tradeStatus));
        }

        // 주소
        if (address != null) {
            predicates.add(cb.equal(article.get("addr1"), address.get(0).getAddr1()));
            predicates.add(cb.equal(article.get("addr2"), address.get(0).getAddr2()));
        }

        cq.select(cb.count(article)).where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getSingleResult();
    }
}
