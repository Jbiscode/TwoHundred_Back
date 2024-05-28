package org.duckdns.bitwatchu.global.auth.oauth;

import java.util.Collections;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return 
        attribute
                .get("id")
                .toString();
    }

    @Override
    public String getEmail() {
              return 
              ((Map<String, Object>) attribute
                                            .getOrDefault("kakao_account", Collections.emptyMap()))
                                            .getOrDefault("email", "")
                                            .toString();
    }

    @Override
    public String getName() {
        return ((Map<String, Object>) attribute
                                            .getOrDefault("properties", Collections.emptyMap()))
                                            .getOrDefault("nickname", "")
                                            .toString();
    }
}

