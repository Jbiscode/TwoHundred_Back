package org.duckdns.bidbuy.app.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.LikeArticle;
import org.duckdns.bidbuy.app.offer.domain.Offer;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.domain.UserRole;

import java.util.List;

@Getter
@AllArgsConstructor
public class MyProfileResponse {
    private Long id;
    private String email;
    private String username;
    private String name;
    private String addr1;
    private String addr2;
    private String provider;
    private String providerId;
    private String profileImageUrl;
    private Integer score;
    private Integer offerLevel;
    private UserRole role;
    private int countSale;
    private int countLike;
    private int countOffer;
    private int countBuy;

    public static MyProfileResponse from(UserDto userDto, int countSale, int countLike, int countOffer, int countBuy) {
        return new MyProfileResponse(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getUsername(),
                userDto.getName(),
                userDto.getAddr1(),
                userDto.getAddr2(),
                userDto.getProvider(),
                userDto.getProviderId(),
                userDto.getProfileImageUrl(),
                userDto.getScore(),
                userDto.getOfferLevel(),
                userDto.getRole(),
                countSale,
                countLike,
                countOffer,
                countBuy
        );
    }


}
