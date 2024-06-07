package org.duckdns.bidbuy.app.user.service;

import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.article.repository.ArticleRepository;
import org.duckdns.bidbuy.app.article.repository.LikeArticleRepository;
import org.duckdns.bidbuy.app.offer.repository.OfferRepository;
import org.duckdns.bidbuy.app.user.dto.MyProfileResponse;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.dto.MySalesResponse;
import org.duckdns.bidbuy.app.user.dto.UserDto;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;
  private final LikeArticleRepository likeArticleRepository;
  private final OfferRepository offerRepository;


    public MyProfileResponse getMyProfile() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getName())
                .addr1(user.getAddr1())
                .addr2(user.getAddr2())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .profileImageUrl(user.getProfileImageUrl())
                .score(user.getScore())
                .offerLevel(user.getOfferLevel())
                .role(user.getRole())
                .build();

        // fetch 가 답인건가요..?
        // 테이블마다 user_id 컬럼에 index 넣자 그러면 조회 좀 빨라짐 왜? 유저 순으로 정렬되니까
        // index로 해결했다 --........
        // fetch + index......... 꺄아아아아아악

        // cache 저장소 운영 - 사실 세션에 담아도 되지만, 클라이언트 종속되니까 안됨
        // cache에 자주 쓰는 데이터 넣어놈, 업데이트함 사용자 잡 정도
        // { userid =1 , {
        //      countarticle: 10,
        //      countlove : 10,
        //
        // } }
        // nosql ..?
        // 쉬운거 1. 다 칼럼으로 만들어서 user 테이블에 소속
        // 쉬운거 2. 인덱스 하나씩만 박고 쏟아지는 select 쿼리 감내
        // 어려운거 1. 처음보는 cache

        int countSale = articleRepository.countByWriter_Id(userId);
        int countLike = likeArticleRepository.countByUser_id(userId);
        int countOffer = offerRepository.countByOfferer_id(userId);
        int countBuy = articleRepository.findSoldOutArticlesByUserId(userId).size();

        return  MyProfileResponse.from(userDto, countSale, countLike, countOffer, countBuy);
    }


    public Page<MySalesResponse> getMySales(TradeStatus status, Pageable pageable) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();

        log.info("pageanle ={}", pageable);
        Page<Object[]> articles = articleRepository.findByWriterIdAndTradeStatus(userId, status, pageable);

        List<MySalesResponse> responses = articles.stream()
                .map(article -> {
                    Long id = (Long) article[0];
                    String title = (String) article[1];
                    Integer price = (Integer) article[2];
                    String addr1 = (String) article[3];
                    String addr2 = (String) article[4];
                    TradeStatus tradeStatus = (TradeStatus) article[5];
                    LocalDateTime createdDate = (LocalDateTime) article[6];
                    String thumbnailUrl = (String) article[7];

                    // 현재 시간과의 차이 계산
                    Duration duration = Duration.between(createdDate, LocalDateTime.now());
                    long seconds = duration.getSeconds();

                    // 적절한 시간 단위로 변환
                    String timeAgo;
                    if (seconds < 60) {
                        timeAgo = seconds + "초 전";
                    } else if (seconds < 3600) {
                        timeAgo = (int) (seconds / 60) + "분 전";
                    } else if (seconds < 86400) {
                        timeAgo = (int) (seconds / 3600) + "시간 전";
                    } else {
                        timeAgo = (int) (seconds / 86400) + "일 전";
                    }

                    return new MySalesResponse(id, title, price, addr1, addr2, tradeStatus, timeAgo, thumbnailUrl);

                })
                .toList();

        return new PageImpl<>(responses, pageable, articles.getTotalElements());
    }
}
