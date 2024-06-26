package org.duckdns.bidbuy.app.user.service;

import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.LikeArticle;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.article.exception.LikeArticleNotFoundException;
import org.duckdns.bidbuy.app.article.repository.ArticleRepository;
import org.duckdns.bidbuy.app.article.repository.LikeArticleRepository;
import org.duckdns.bidbuy.app.article.service.ImageUploadService;
import org.duckdns.bidbuy.app.offer.repository.OfferRepository;
import org.duckdns.bidbuy.app.review.repository.ReviewRepository;
import org.duckdns.bidbuy.app.user.dto.*;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.exception.ForbiddenException;
import org.duckdns.bidbuy.app.user.exception.NotLoggedInException;
import org.duckdns.bidbuy.app.user.exception.PasswordLengthException;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.CustomUserDetails;
import org.duckdns.bidbuy.global.error.NullInputException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final LikeArticleRepository likeArticleRepository;
    private final OfferRepository offerRepository;
    private final ReviewRepository reviewRepository;
    private final ImageUploadService imageUploadService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


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
        int countBuy = offerRepository.countBuy(userId);
        int countReview = reviewRepository.countByRevieweeIdOrReviewerId(userId,userId);

        return  MyProfileResponse.from(userDto, countSale, countLike, countOffer, countBuy,countReview);
    }

    public MyProfileResponse getUserProfile(Long userId) {

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

        int countSale = articleRepository.countByWriter_Id(userId);
        int countLike = likeArticleRepository.countByUser_id(userId);
        int countOffer = offerRepository.countByOfferer_id(userId);
        int countBuy = offerRepository.countBuy(userId);
        int countReview = reviewRepository.countByRevieweeIdOrReviewerId(userId,userId);

        return  MyProfileResponse.from(userDto, countSale, countLike, countOffer, countBuy,countReview);
    }


    public PageResponseDTO getMySales(String sorting,TradeStatus status, Pageable pageable) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Page<MySalesResponse> articles;
        switch (sorting) {
            case "latest":
                articles = articleRepository.findByWriterIdAndTradeStatus(userId, status, pageable);
                break;
            case "high-price":
                articles = articleRepository.findByWriterIdAndTradeStatusOrderByPriceDESC(userId, status, pageable);
                break;
            case "low-price":
                articles =  articleRepository.findByWriterIdAndTradeStatusOrderByPriceASC(userId, status, pageable);
                break;
            default:
                articles = articleRepository.findByWriterIdAndTradeStatus(userId, status, pageable);
        }

        List<MySalesResponse> responses = articles.getContent();

        PageResponseDTO pageResponseDTO = new PageResponseDTO(responses, pageable, articles.getTotalElements());

        return pageResponseDTO;
    }

    public PageResponseDTO<List<MySalesResponse>> getUserSales(Long userId, TradeStatus tradeStatus, String sorting,Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        Page<MySalesResponse> articles = null;

        try {
            var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails customUserDetails) {
                Long loggedInUserId = customUserDetails.getUser().getId();
                switch (sorting) {
                    case "latest":
                        articles = articleRepository.findByWriterIdAndTradeStatusUserLoggedIn(userId, loggedInUserId, tradeStatus, pageable);
                        break;
                    case "high-price":
                        articles = articleRepository.findByWriterIdAndTradeStatusUserLoggedInOrderByPriceDESC(userId, loggedInUserId, tradeStatus, pageable);
                        break;
                    case "low-price":
                        articles =  articleRepository.findByWriterIdAndTradeStatusUserLoggedInOrderByPriceASC(userId, loggedInUserId, tradeStatus, pageable);
                        break;
                    default:
                        articles = articleRepository.findByWriterIdAndTradeStatusUserLoggedIn(userId, loggedInUserId, tradeStatus, pageable);
                }
            }else if(principal instanceof String){
                switch (sorting) {
                    case "latest":
                        articles =  articleRepository.findByWriterIdAndTradeStatusUser(userId, tradeStatus, pageable);
                        break;
                    case "high-price":
                        articles = articleRepository.findByWriterIdAndTradeStatusUserOrderByPriceDESC(userId, tradeStatus, pageable);
                        break;
                    case "low-price":
                        articles =  articleRepository.findByWriterIdAndTradeStatusUserOrderByPriceASC(userId, tradeStatus, pageable);
                        break;
                    default:
                        articles = articleRepository.findByWriterIdAndTradeStatusUser(userId, tradeStatus, pageable);
                }
            }

        } catch (Exception e) {
            log.error("Error getting user sales: {}", e.getMessage());
            throw new RuntimeException("Error getting user sales", e);
        }
        List<MySalesResponse> responses = articles.getContent();
        PageResponseDTO pageResponseDTO = new PageResponseDTO(responses, pageable, articles.getTotalElements());

        return pageResponseDTO;
    }

    public PageResponseDTO getLikeArticles(String sort, Pageable pageable) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

//        Page<Object[]> articles = articleRepository.findArticlesByUserIdWithLikes(userId,pageable);
        Page<MySalesResponse> articles;
        switch (sort) {
            case "latest":
                articles = articleRepository.findArticlesByUserIdWithLikes(userId, pageable);
                break;
            case "high-price":
                articles = articleRepository.findArticlesByUserIdWithLikesOrderByPriceDesc(userId, pageable);
                break;
            case "low-price":
                articles = articleRepository.findArticlesByUserIdWithLikesOrderByPriceAsc(userId, pageable);
                break;
            default:
                articles = articleRepository.findArticlesByUserIdWithLikes(userId, pageable);
        }
        List<MySalesResponse> responses = articles.getContent();

        PageResponseDTO pageResponseDTO = new PageResponseDTO(responses, pageable, articles.getTotalElements());
        return pageResponseDTO;
    }

    public PageResponseDTO<List<MySalesResponse>> getMyOffers(TradeStatus tradeStatus, String sorting,Pageable pageable) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Page<MySalesResponse> articles;
        switch (sorting) {
            case "latest":
                articles = articleRepository.getOfferedArticlesByUserId(userId, pageable);
                break;
            case "high-price":
                articles = articleRepository.getOfferedArticlesByUserIdOrderByPriceDESC(userId, pageable);
                break;
            case "low-price":
                articles =  articleRepository.getOfferedArticlesByUserIdOrderByPriceASC(userId, pageable);
                break;
            default:
                articles = articleRepository.getOfferedArticlesByUserId(userId, pageable);
        }
        List<MySalesResponse> responses = articles.getContent();
        PageResponseDTO pageResponseDTO = new PageResponseDTO(responses, pageable, articles.getTotalElements());
        return pageResponseDTO;
    }

    @Transactional
    public String updateLikeArticles(Long articleId) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Principal: " + principal);
        Long userId = null;
        if (principal instanceof CustomUserDetails customUserDetails) {
            userId = customUserDetails.getUser().getId();
            log.info("로그인 상태.");
        }else if(principal instanceof String) {
            throw new NotLoggedInException("찜하려면 로그인 하세요");
        }

        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Optional<LikeArticle> likeArticles = likeArticleRepository.findByArticleIdAndUserId(articleId, userId);
        Optional<Article> article = articleRepository.findById(articleId);
        if(article.isEmpty()) throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        if(article.get().getWriter().getId().equals(userId)) throw new ForbiddenException("본인의 게시글는 찜할 수 없습니다.");

        if(likeArticles.isPresent()){
            likeArticleRepository.deleteByArticleIdAndUserId(articleId, userId);
            article.get().minusLikeCount();
            return "찜한 상품을 목록에서 제거했습니다.";
        }else{
            LikeArticle likeArticle = LikeArticle.builder()
                    .user(User.builder().id(userId).build())
                    .article(Article.builder().id(articleId).build())
                    .build();

            article.get().plusLikeCount();
            likeArticleRepository.save(likeArticle);
            return "상품을 찜목록에 등록했습니다.";
        }
    }

    public PageResponseDTO<List<MySalesResponse>> getMyBuys(TradeStatus tradeStatus,String sorting, Pageable pageable) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();
        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Page<MySalesResponse> articles;
        switch (sorting) {
            case "latest":
//                log.error("나 맞지?");
                articles = articleRepository.getOfferedArticlesByUserIdAndIsSelected(userId, pageable);
                break;
            case "high-price":
                articles = articleRepository.getOfferedArticlesByUserIdAndIsSelectedOrderByPriceDESC(userId, pageable);
                break;
            case "low-price":
                articles =  articleRepository.getOfferedArticlesByUserIdAndIsSelectedOrderByPriceASC(userId, pageable);
                break;
            default:
                articles = articleRepository.getOfferedArticlesByUserIdAndIsSelected(userId, pageable);
        }
        List<MySalesResponse> responses = articles.getContent();
        PageResponseDTO pageResponseDTO = new PageResponseDTO(responses, pageable, articles.getTotalElements());
        return pageResponseDTO;
    }

    // 현재 시간과의 차이 계산
    private String getTimeAgo(LocalDateTime createdDate) {
        Duration duration = Duration.between(createdDate, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + "초 전";
        } else if (seconds < 3600) {
            return (int) (seconds / 60) + "분 전";
        } else if (seconds < 86400) {
            return (int) (seconds / 3600) + "시간 전";
        } else {
            return (int) (seconds / 86400) + "일 전";
        }
    }


    public MyInfoResponse getMyInfo() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();
        User user = userRepository.findById(userId).get();

        return MyInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .addr1(user.getAddr1())
                .password("")
                .addr2(user.getAddr2())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    @Transactional
    public String updateUserInfo(MyInfoResponse myInfoResponseDTO, List<MultipartFile> list) throws IOException {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();
        User user = userRepository.findById(userId).get();
//        log.error("userId = {}",userId);
        if (myInfoResponseDTO.getEmail() == null || myInfoResponseDTO.getEmail().isEmpty()
                || myInfoResponseDTO.getPassword() == null || myInfoResponseDTO.getPassword().isEmpty()
                || myInfoResponseDTO.getUsername() == null || myInfoResponseDTO.getUsername().isEmpty()
                || myInfoResponseDTO.getAddr1() == null || myInfoResponseDTO.getAddr1().isEmpty()
                || myInfoResponseDTO.getAddr2() == null || myInfoResponseDTO.getAddr2().isEmpty()) {
            throw new NullInputException("입력값을 확인해주세요.");
        }

        if(myInfoResponseDTO.getPassword().length() < 8) {
            throw new PasswordLengthException("비밀번호의 길이가 짧습니다.");
        }

        String imgUrl = "";
        if(list == null) {
            imgUrl = user.getProfileImageUrl();
        }else{
            if(!user.getProfileImageUrl().equals("s_uuid_7adc2b20-82c8-4f14-96f0-f68aa2613ac0")) {
                imageUploadService.deleteImage(user.getProfileImageUrl());
            }
            MultipartFile[] multipartFiles = list.toArray(new MultipartFile[0]);
            List<Map<String, String>> uploadimageURL = imageUploadService.uploadImages(multipartFiles);
            for (int i = 0; i < uploadimageURL.size(); i++) {
                Map<String, String> imageUrlMap = uploadimageURL.get(i);
                imgUrl = imageUrlMap.get("thumbnail");
            }
        }

        user.update(myInfoResponseDTO.getUsername(), myInfoResponseDTO.getAddr1(), myInfoResponseDTO.getAddr2(), bCryptPasswordEncoder.encode(myInfoResponseDTO.getPassword()), imgUrl);

        userRepository.save(user);
        return "회원정보 수정에 성공했습니다.";
    }
}
