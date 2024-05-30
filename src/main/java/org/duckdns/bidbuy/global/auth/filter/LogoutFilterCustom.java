package org.duckdns.bidbuy.global.auth.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.duckdns.bidbuy.global.auth.domain.RefreshTokenRepository;
import org.duckdns.bidbuy.global.auth.jwt.JWTUtil;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


public class LogoutFilterCustom extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public LogoutFilterCustom(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");

        // /logout요청이 POST Method로 들어왔는지 확인
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/api\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        //refresh token 가져오기
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }
        }

        //refresh token null check
        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals("refresh")) {

            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if (!isExist) {
            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        //json
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        ApiResponse<String> apiResponse = new ApiResponse<>("200", "로그아웃 성공", null);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}