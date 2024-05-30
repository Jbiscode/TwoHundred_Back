package org.duckdns.bidbuy.global.auth.oauth;

import org.duckdns.bidbuy.global.auth.domain.LoginResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class OAuth2UserCustom implements OAuth2User {

    private final LoginResponse loginResponse;


    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) loginResponse::role);
        return collection;
    }

    @Override
    public String getName() {
        return loginResponse.name();
    }

    public String getUsername() {
        return loginResponse.username();
    }

    public String getEmail() {
        return loginResponse.email();
    }
}

