package org.duckdns.bidbuy.app.offer.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.repository.ArticleRepository;
import org.duckdns.bidbuy.app.offer.domain.Offer;
import org.duckdns.bidbuy.app.offer.dto.OfferRequest;
import org.duckdns.bidbuy.app.offer.dto.OfferResponse;
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

        User offerer = userRepository.findById(offererId).orElseThrow(() -> new RuntimeException("User not found"));
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new RuntimeException("Article not found"));

        Offer offer = Offer.builder()
                .price(requestDTO.getPrice())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .offerer(offerer)
                .article(article)
                .build();

        offerRepository.save(offer);

        return new OfferResponse(
                offer.getId(),
                offer.getArticle().getId(),
                offer.getOfferer().getUsername(),
                offer.getOfferer().getAddr1(),
                offer.getOfferer().getAddr2(),
                offer.getCreatedDate(),
                offer.getPrice()
        );

    }

    public List<OfferResponse> getOffersByArticleId(Long articleId) {
        List<Offer> offers = offerRepository.findByArticleId(articleId);

        return offers.stream().map(offer -> new OfferResponse(
            offer.getId(),
            offer.getArticle().getId(),
            offer.getOfferer().getUsername(),
            offer.getOfferer().getAddr1(),
            offer.getOfferer().getAddr2(),
            offer.getCreatedDate(),
            offer.getPrice()
        )).collect(Collectors.toList());
    }

}
