package org.duckdns.bitwatchu.global.auth.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.duckdns.bitwatchu.global.auth.domain.LoginRequest;
import org.duckdns.bitwatchu.global.auth.domain.RefreshTokenEntity;
import org.duckdns.bitwatchu.global.auth.domain.RefreshTokenRepository;
import org.duckdns.bitwatchu.global.auth.jwt.JWTUtil;
import org.duckdns.bitwatchu.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginRequest = objectMapper.readValue(messageBody, LoginRequest.class);

        } catch (IOException e) {
            log.error("로그인 요청을 처리하는 동안 오류가 발생했습니다.", e);
            throw new RuntimeException(e);
        }

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        log.info("username: {}" , username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        //유저 정보
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 60*60*1000L);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //Refresh 토큰 저장
        addRefreshTokenEntity(username, refreshToken, 86400000L);

        //응답 설정
//        response.setHeader("Access-Control-Expose-Headers", "Authorization, refresh, access, Set-Cookie");
        response.setHeader("Authorization", "Bearer " + access);
        // // 로컬 스토리지에 Authorization 저장
        // response.setHeader("Set-Cookie", "Authorization=Bearer " + access + "; path=/; max-age=600; HttpOnly");
        // refresh토큰을 헤더로 보내서 토큰으로 저장
        response.setHeader("Set-Cookie", "refresh=" + refreshToken + "; path=/; max-age=86400; SameSite=None;  HttpOnly");
        // response.addCookie(createCookie("refresh", refreshToken));
        ApiResponse<String> apiResponse = new ApiResponse<>("200", "로그인 성공", null);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        log.error("로그인 실패: {}", failed.getMessage());
        ApiResponse<String> apiResponse = new ApiResponse<>("400", "로그인 실패", null);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }

    private void addRefreshTokenEntity(String username, String refreshToken, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                                                            .username(username)
                                                            .refreshToken(refreshToken)
                                                            .expiration(date.toString())
                                                            .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
