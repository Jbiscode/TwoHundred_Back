package org.duckdns.bidbuy.app.user.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.LikeArticle;
import org.duckdns.bidbuy.app.offer.domain.Offer;
import org.duckdns.bidbuy.app.review.domain.Review;
import org.duckdns.bidbuy.app.user.dto.UserDto;
import org.duckdns.bidbuy.global.common.entity.BaseEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String username;
    private String name;

    private String addr1;
    private String addr2;

    private String provider;
    private String providerId;
    private String profileImageUrl;
    @ColumnDefault("0")
    private Integer score;

    @ColumnDefault("1")
    private Integer offerLevel;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "writer")
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "offerer")
    private List<Offer> offers = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer")
    private List<Review> reviewsGiven = new ArrayList<>();

    @OneToMany(mappedBy = "reviewee")
    private List<Review> reviewsReceived = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<LikeArticle> likedArticles = new ArrayList<>();


    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void update(String username, String addr1, String addr2, String password, String profileImageUrl) {
        this.username = username;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.setModifiedDate(LocalDateTime.now());
    }

    public void updateScore(Integer score) {
        this.score += score;
        if (this.score < 10) {
            this.offerLevel = 1;
        } else if (this.score < 50) {
            this.offerLevel = 2;
        }else if (this.score < 100) {
            this.offerLevel = 3;
        }else if (this.score < 150) {
            this.offerLevel = 4;
        }else {
            this.offerLevel = 5;
        }

    }




}
