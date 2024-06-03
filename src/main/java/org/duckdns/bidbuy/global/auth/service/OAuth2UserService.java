package org.duckdns.bidbuy.global.auth.service;

import java.util.Optional;

import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.LoginResponse;
import org.duckdns.bidbuy.global.auth.oauth.GoogleResponse;
import org.duckdns.bidbuy.global.auth.oauth.KakaoResponse;
import org.duckdns.bidbuy.global.auth.oauth.NaverResponse;
import org.duckdns.bidbuy.global.auth.oauth.OAuth2Response;
import org.duckdns.bidbuy.global.auth.oauth.OAuth2UserCustom;
import org.duckdns.bidbuy.app.user.domain.UserRole;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2User: {}", oAuth2User.getAttributes().toString());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        switch (registrationId) {
            case "naver" -> oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
            case "google" -> oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
            case "kakao" -> oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
            default -> {
                return null;
            }
        }
        
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Optional<User> existData = userRepository.findByUsername(username);

        if (existData.isEmpty()) {

            User user =  User.builder()
                                                  .username(username)
                                                  .email(oAuth2Response.getEmail())
                                                  .name(oAuth2Response.getName())
                                                  .role(UserRole.USER)
                                                  .build();

            userRepository.save(user);

            LoginResponse userDTO = new LoginResponse(user.getId(), user.getRole(), username, oAuth2Response.getName(), oAuth2Response.getEmail());

            return new OAuth2UserCustom(userDTO);
        } else {
            User user = existData.get();
            user.update(oAuth2Response.getName(), oAuth2Response.getEmail());

            userRepository.save(user);

            LoginResponse userDTO = new LoginResponse(existData.get().getId(),existData.get().getRole(), existData.get().getUsername(), oAuth2Response.getName(), oAuth2Response.getEmail());

            return new OAuth2UserCustom(userDTO);
        }
    }
}
