package org.duckdns.bidbuy.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.global.auth.domain.CustomUserDetails;
import org.duckdns.bidbuy.global.auth.domain.LoginRequest;
import org.duckdns.bidbuy.global.auth.domain.LoginResponse;
import org.duckdns.bidbuy.global.auth.domain.RefreshTokenEntity;
import org.duckdns.bidbuy.global.auth.domain.RefreshTokenRepository;
import org.duckdns.bidbuy.global.auth.jwt.JWTUtil;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/login");  // 로그인 처리 URL 설정
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

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        log.info("email: {}" , email);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        //유저 정보
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Long userId = user.getId();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String username = user.getUsername();


        //토큰 생성
        String access = jwtUtil.createJwt("access",userId, username, role, 60*60*1000L);
        String refreshToken = jwtUtil.createJwt("refresh",userId, username, role, 86400000L);

        //Refresh 토큰 저장
        addRefreshTokenEntity(userId, username, refreshToken, 86400000L);

        //응답 설정
//        response.setHeader("Access-Control-Expose-Headers", "Authorization, refresh, access, Set-Cookie");
        response.setHeader("Authorization", "Bearer " + access);
        // // 로컬 스토리지에 Authorization 저장
        // response.setHeader("Set-Cookie", "Authorization=Bearer " + access + "; path=/; max-age=600; HttpOnly");
        // refresh토큰을 헤더로 보내서 토큰으로 저장
        response.setHeader("Set-Cookie", "refresh=" + refreshToken + "; path=/; max-age=86400; SameSite=None; Secure; HttpOnly");
        // response.addCookie(createCookie("refresh", refreshToken));
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>("200", "로그인 성공", new LoginResponse(userId, user.getRole(), username, user.getName(), user.getEmail()));
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

    private void addRefreshTokenEntity(Long userId,String username, String refreshToken, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);
        if(refreshTokenRepository.existsByUserId(userId)) {
            refreshTokenRepository.deleteByUserId(userId);
        }
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                                                            .userId(userId)
                                                            .userName(username)
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
