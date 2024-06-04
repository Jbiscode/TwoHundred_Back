package org.duckdns.bidbuy.repository;

import lombok.extern.log4j.Log4j2;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.Category;
import org.duckdns.bidbuy.app.article.domain.TradeMethod;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.article.repository.ArticleRepository;
import org.duckdns.bidbuy.app.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class ArticleRepositoryTests {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testInsert(){

        for(int i=1; i<=10; i++){
            Article article = Article.builder()
                    .title("제목..." + i)
                    .content("내용...." + i)
                    .price(1000 * i)
                    .quantity(10)
                    .likeCount(0L)
                    .viewCount(0L)
                    .category(Category.FOOD)
                    .tradeMethod(TradeMethod.FACE_TO_FACE)
                    .tradeStatus(TradeStatus.ON_SALE)
                    .addr1("서울시")
                    .addr2("강남구")
                    .createdDate(LocalDateTime.now())
                    .modifiedDate(LocalDateTime.now())
                    .writer(User.builder().id((long)1).build())
                    .build();

            articleRepository.save(article);
        }

    }

    //한 개의 게시글을 읽어오는 테스트
    @Test
    @Transactional
    public void testRead(){

        Long id = 1L;

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No article found with id: " + id));
        log.info(article);

    }

    // 모든 게시글을 읽어오는 테스트
    @Test
    @Transactional
    public void testReadAll(){

        articleRepository.findAll().forEach(article -> log.info(article));

    }

    // 특정 게시글을 수정하는 테스트
    @Test
    @Transactional
    @Rollback(false)
    public void testUpdate(){

        Long id = 1L;

        Optional<Article> result = articleRepository.findById(id);

        Article article = result.orElseThrow();
        article.update("수정된 제목", "수정된 내용", 10000, 10, "서울시", "강남구", Category.FOOD, TradeMethod.FACE_TO_FACE, TradeStatus.ON_SALE);

        articleRepository.save(article);

    }

    @Test
    public void testDelete(){

        Long id = 10L;

        articleRepository.deleteById(id);
    }

    // 페이징 처리 테스트
    @Test
    @Transactional
    public void testPaging(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("id").descending());

        Page<Article> result = articleRepository.findAll(pageable);

        log.info(result.getTotalElements());

        result.getContent().stream().forEach(article -> log.info(article));

    }

}
