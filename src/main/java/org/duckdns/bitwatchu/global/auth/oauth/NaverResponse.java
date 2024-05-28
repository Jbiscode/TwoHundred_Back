package org.duckdns.bitwatchu.global.auth.oauth;


import java.util.Collections;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class NaverResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return ((Map<String, Object>) attribute
                                            .getOrDefault("response", Collections.emptyMap()))
                                            .getOrDefault("id", "")
                                            .toString();
    }

    @Override
    public String getEmail() {
        return ((Map<String, Object>) attribute
                                            .getOrDefault("response", Collections.emptyMap()))
                                            .getOrDefault("email", "")
                                            .toString();
    }

    @Override
    public String getName() {
        return ((Map<String, Object>) attribute
                                            .getOrDefault("response", Collections.emptyMap()))
                                            .getOrDefault("name", "")
                                            .toString();
    }
}

