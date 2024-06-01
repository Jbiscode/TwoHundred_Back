package org.duckdns.bidbuy.global.auth.service;

import java.util.Optional;

import org.duckdns.bidbuy.app.user.domain.UserEntity;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.LoginResponse;
import org.duckdns.bidbuy.global.auth.oauth.GoogleResponse;
import org.duckdns.bidbuy.global.auth.oauth.KakaoResponse;
import org.duckdns.bidbuy.global.auth.oauth.NaverResponse;
import org.duckdns.bidbuy.global.auth.oauth.OAuth2Response;
import org.duckdns.bidbuy.global.auth.oauth.OAuth2UserCustom;
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
        Optional<UserEntity> existData = userRepository.findByUsername(username);

        if (existData.isEmpty()) {

            UserEntity userEntity =  UserEntity.builder()
                                                  .username(username)
                                                  .email(oAuth2Response.getEmail())
                                                  .name(oAuth2Response.getName())
                                                  .role("USER")
                                                  .build();

            userRepository.save(userEntity);

            LoginResponse userDTO = new LoginResponse("USER", username, oAuth2Response.getName(), oAuth2Response.getEmail());

            return new OAuth2UserCustom(userDTO);
        } else {
            UserEntity userEntity = existData.get();
            userEntity.update(oAuth2Response.getName(), oAuth2Response.getEmail());

            userRepository.save(userEntity);

            LoginResponse userDTO = new LoginResponse(existData.get().getRole(), existData.get().getUsername(), oAuth2Response.getName(), oAuth2Response.getEmail());

            return new OAuth2UserCustom(userDTO);
        }
    }
}
