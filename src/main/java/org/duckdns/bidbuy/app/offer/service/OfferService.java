package org.duckdns.bidbuy.app.offer.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.TradeStatus;
import org.duckdns.bidbuy.app.article.exception.ArticleNotExistException;
import org.duckdns.bidbuy.app.article.repository.ArticleRepository;
import org.duckdns.bidbuy.app.offer.domain.Offer;
import org.duckdns.bidbuy.app.offer.dto.OfferAcceptResponse;
import org.duckdns.bidbuy.app.offer.dto.OfferRequest;
import org.duckdns.bidbuy.app.offer.dto.OfferResponse;
import org.duckdns.bidbuy.app.offer.exception.OfferExceedException;
import org.duckdns.bidbuy.app.offer.exception.OffererNotFoundException;
import org.duckdns.bidbuy.app.offer.repository.OfferRepository;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    //게시글에 대한 가격 제안
    public OfferResponse createOffer(Long articleId, OfferRequest requestDTO) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long offererId = principal.getUser().getId();

        User offerer = userRepository.findById(offererId).orElseThrow(() -> new OffererNotFoundException(offererId));
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotExistException(articleId));

        //게시글의 가격보다 높은 가격에는 제안할 수 없음
        if (requestDTO.getPrice() > article.getPrice()) {
            throw new OfferExceedException(article.getPrice());
        }

        Offer offer = Offer.builder()
                .price(requestDTO.getPrice())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .offerer(offerer)
                .article(article)
                .isSelected(false)
                .build();

        offerRepository.save(offer);

        return new OfferResponse(
                offer.getId(),
                offer.getArticle().getId(),
                offer.getOfferer().getId(),
                offer.getOfferer().getUsername(),
                offer.getOfferer().getAddr1(),
                offer.getOfferer().getAddr2(),
                offer.getCreatedDate(),
                offer.getPrice(),
                offer.isSelected()
        );

    }

    public List<OfferResponse> getOffersByArticleId(Long articleId) {
        List<Offer> offers = offerRepository.findByArticleId(articleId);

        return offers.stream().map(offer -> new OfferResponse(
                offer.getId(),
                offer.getArticle().getId(),
                offer.getOfferer().getId(),
                offer.getOfferer().getUsername(),
                offer.getOfferer().getAddr1(),
                offer.getOfferer().getAddr2(),
                offer.getCreatedDate(),
                offer.getPrice(),
                offer.isSelected()
        )).collect(Collectors.toList());
    }

    //게시글에 대한 가격 제안 수락
    public OfferAcceptResponse acceptOffer(Long offerId, Long articleId) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();

        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new IllegalArgumentException("제안이 존재하지 않습니다."));
        if (!offer.getArticle().getId().equals(articleId)) {
            throw new IllegalArgumentException("제안의 게시글 ID와 일치하지 않습니다.");
        }
        if (!offer.getArticle().getWriter().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글 작성자만 제안을 수락할 수 있습니다.");
        }

        Offer updatedOffer = Offer.builder()
                .id(offerId)
                .price(offer.getPrice())
                .createdDate(offer.getCreatedDate())
                .modifiedDate(offer.getModifiedDate())
                .offerer(offer.getOfferer())
                .article(offer.getArticle())
                .isSelected(true)
                .build();

        offerRepository.save(updatedOffer);

        Article article = offer.getArticle();
        article = Article.builder()
                .id(articleId)
                .title(article.getTitle())
                .content(article.getContent())
                .price(article.getPrice())
                .quantity(article.getQuantity())
                .writer(article.getWriter())
                .createdDate(article.getCreatedDate())
                .modifiedDate(article.getModifiedDate())
                .category(article.getCategory())
                .likeCount(article.getLikeCount())
                .viewCount(article.getViewCount())
                .addr1(article.getAddr1())
                .addr2(article.getAddr2())
                .tradeMethod(article.getTradeMethod())
                .tradeStatus(TradeStatus.RESERVED)
                .build();

        articleRepository.save(article);

        return new OfferAcceptResponse(
                updatedOffer.getId(),
                updatedOffer.getPrice(),
                updatedOffer.isSelected()
        );
    }

    public void cancelOffer(Long offerId) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long offererId = principal.getUser().getId();

        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new IllegalArgumentException("제안이 존재하지 않습니다."));
        if (!offer.getOfferer().getId().equals(offererId)) {
            throw new IllegalArgumentException("제안 당사자만 취소할 수 있습니다.");
        }
        if (offer.isSelected()) {
            throw new IllegalArgumentException("수락된 제안은 취소할 수 없습니다.");
        }
        offerRepository.delete(offer);
    }

    public void cancelAcceptedOffer(Long offerId, Long articleId) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();

        Offer offer = offerRepository.findById(offerId).orElseThrow(() -> new IllegalArgumentException("제안이 존재하지 않습니다."));
        if (!offer.getArticle().getId().equals(articleId)) {
            throw new IllegalArgumentException("제안의 게시글 ID와 일치하지 않습니다.");
        }
        if (!offer.getArticle().getWriter().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글 작성자만 제안을 취소할 수 있습니다.");
        }
        if (!offer.getArticle().getTradeStatus().equals(TradeStatus.RESERVED)) {
            throw new IllegalArgumentException("예약 상태인 제안만 취소할 수 있습니다.");
        }

        Article article = offer.getArticle();
        article.update(article.getTitle(), article.getContent(), article.getPrice(), article.getQuantity(),
                article.getAddr1(), article.getAddr2(), article.getCategory(), article.getTradeMethod(), TradeStatus.ON_SALE);
        articleRepository.save(article);

        offer.update(false);
    }

    public void completeSale(Long articleId) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();

        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotExistException(articleId));
        if (!article.getWriter().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글 작성자만 판매를 완료할 수 있습니다.");
        }
        if (!article.getTradeStatus().equals(TradeStatus.RESERVED)) {
            throw new IllegalArgumentException("예약 상태인 게시글만 판매를 완료할 수 있습니다.");
        }

        article.update(article.getTitle(), article.getContent(), article.getPrice(), article.getQuantity(),
                article.getAddr1(), article.getAddr2(), article.getCategory(), article.getTradeMethod(), TradeStatus.SOLD_OUT);
        articleRepository.save(article);
    }

}
