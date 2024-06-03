package org.duckdns.bidbuy.app.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.duckdns.bidbuy.app.article.domain.Article;
import org.duckdns.bidbuy.app.article.domain.LikeArticle;
import org.duckdns.bidbuy.app.offer.domain.Offer;
import org.duckdns.bidbuy.app.review.domain.Review;
import org.duckdns.bidbuy.global.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    private String provider;
    private String providerId;
    private String profileImageUrl;
    private Integer score;
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
}
