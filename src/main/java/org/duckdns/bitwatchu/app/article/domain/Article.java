package org.duckdns.bitwatchu.app.article.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.duckdns.bitwatchu.app.offer.domain.Offer;
import org.duckdns.bitwatchu.app.user.domain.UserEntity;
import org.duckdns.bitwatchu.global.common.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "article")
public class Article extends BaseEntity {


    private String title;

    private String content;

    private int likeCount;

    private String mainImageUrl;

    private String thumbImageUrl;

    private int price;

    private int quantity;

    private String address1;

    private String address2;

    private TradeMethodStatus tradeMethodStatus;

    private TradeStatus tradeStatus;

    private int viewCount;

    @OneToMany(mappedBy = "article")
    private List<Offer> offers = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name ="user_id")
    private UserEntity writer;


}
