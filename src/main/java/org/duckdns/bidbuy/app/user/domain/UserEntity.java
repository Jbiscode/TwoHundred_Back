package org.duckdns.bidbuy.app.user.domain;

import jakarta.persistence.*;
import org.duckdns.bidbuy.global.common.entity.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "users")
public class UserEntity extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


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