package org.duckdns.bitwatchu.app.user.domain;

import org.duckdns.bitwatchu.global.common.entity.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
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
@Table(name = "user")
public class UserEntity extends BaseEntity{



    @Column(unique = true)
    private String username;
    private String name;

    @JsonIgnore
    private String password;
    private String email;
    private String role;

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }
}