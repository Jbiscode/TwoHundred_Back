package org.duckdns.bidbuy.global.auth.domain;

import org.duckdns.bidbuy.global.common.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "refresh_token")
public class RefreshTokenEntity extends BaseEntity{

    private String username;
    private String refreshToken;
    private String expiration;

}
