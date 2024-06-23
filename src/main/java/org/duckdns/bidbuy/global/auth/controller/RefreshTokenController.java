package org.duckdns.bidbuy.global.auth.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.global.auth.domain.RefreshTokenEntity;
import org.duckdns.bidbuy.global.auth.domain.RefreshTokenRepository;
import org.duckdns.bidbuy.global.auth.jwt.JWTUtil;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;

@Controller
@ResponseBody
@Slf4j
@RequestMapping("/api")
public class RefreshTokenController {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenController(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {

        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("refreshToken 시작");
        //get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refreshToken = cookie.getValue();
                    log.info("refreshToken?: {}", refreshToken);
                }
            }
        }

        if (refreshToken == null) {

            //response status code
            return new ResponseEntity<>("refresh token 이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token 이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refreshToken);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("잘못된 refresh token 입니다.", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        log.info("isExist 존재?: {}", isExist);
        if (!isExist) {
            //로그아웃 진행

            //Refresh 토큰 Cookie 값 0
            Cookie cookie = new Cookie("refresh", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");

            response.addCookie(cookie);
            //json
//            response.setContentType("application/json");
//            response.setStatus(HttpServletResponse.SC_OK);
            ApiResponse<String> apiResponse = new ApiResponse<>("400", "다른곳에서 로그인 되었습니다.", null);
//            response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
            return ResponseEntity.ok(apiResponse);
        }

        Long userId = jwtUtil.getUserId(refreshToken);
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", userId, username, role, 60*60*1000L); // 일단 1시간
//        String newRefreshToken = jwtUtil.createJwt("refresh", userId, username, role, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
//        refreshTokenRepository.deleteByRefreshToken(refreshToken);
//        addRefreshEntity(userId, username, newRefreshToken, 86400000L);

        //response
        response.setHeader("Authorization", "Bearer " + newAccess);
//        response.addCookie(createCookie("Authorization", newAccess, 600000L));
//        response.addCookie(createCookie("refresh", newRefreshToken, 86400000L));

        System.out.println("토큰 재발급 성공");
        return ResponseEntity.ok(new ApiResponse<>("200", "토큰 재발급 성공", null));
    }

    private void addRefreshEntity(Long userId, String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshTokenEntity refreshEntity = RefreshTokenEntity.builder()
                .userId(userId)
                .userName(username)
                .refreshToken(refresh)
                .expiration(date.toString()).build();


        refreshTokenRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value, Long expiredMs) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiredMs.intValue()/1000);
        //cookie.setSecure(true);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
