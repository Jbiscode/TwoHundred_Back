package org.duckdns.bitwatchu.global.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.bitwatchu.app.user.domain.UserEntity;
import org.duckdns.bitwatchu.global.auth.domain.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @SuppressWarnings({"NullableProblems", "null"})
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        String accessToken = "";
        // 헤더에서 Authorization 키에 담긴 토큰을 꺼냄
        String authorization = request.getHeader("Authorization");
        String refreshToken = "";

        log.info("authorization1: {}", authorization);
        if (authorization != null && authorization.startsWith("Bearer ")) {
            accessToken = authorization.split(" ")[1];
        }
        log.info("accessToken1: {}", accessToken);

        if (accessToken.isEmpty() || !authorization.startsWith("Bearer")){
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("Authorization")) {
                        accessToken = cookie.getValue();
                        log.info("accessToken: {}", accessToken);
                    }
                    if (cookie.getName().equals("refresh")) {
                        refreshToken = cookie.getValue();
                        log.info("refreshToken: {}", refreshToken);
                }
            }
        }
        }

        if (accessToken.isEmpty()) {
            log.info("token 이 없습니다?");
            // 권한없음

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음 // 여기는 CustomizedResponseEntityExceptionHandler 가 접근하기 전이라 직접 처리해야함
        try {
            if (refreshToken.isEmpty()) {
                jwtUtil.isExpired(accessToken);
            }
        } catch (ExpiredJwtException e) {

            //response body
            log.error("토큰이 만료되었습니다.");
//            response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"timestamp\": \"" + new Date() + "\""
                    + ",\"message\": \"토큰이 만료되었습니다.\""
                    + ",\"details\": \"" + e.getMessage() + "\"}");
//            filterChain.doFilter(request, response);
            return;

        } catch (MalformedJwtException e) {
            log.error("토큰 형식이 올바르지 않습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"timestamp\": \"" + new Date() + "\""
                    + ",\"message\": \"토큰 형식이 올바르지 않습니다.\""
                    + ",\"details\": \"" + e.getMessage() + "\"}");
            return;

        } catch (SignatureException e) {
            //response body
            log.error("토큰 서명이 올바르지 않습니다.");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"timestamp\": \"" + new Date() + "\""
                    + ",\"message\": \"토큰 서명이 올바르지 않습니다.\""
                    + ",\"details\": \"" + e.getMessage() + "\"}");
            return;
        }
        if (!refreshToken.isEmpty()) {
            String refreshCategory = jwtUtil.getCategory(refreshToken);
            if (refreshCategory.equals("refresh")) {
                return;
            }
        }

        // 토큰 카테고리가 access 인지 확인 (발급시 페이로드에 명시)
            String category = jwtUtil.getCategory(accessToken);


            if (!category.equals("access")) {
                //response body
                PrintWriter writer = response.getWriter();
                writer.print("access 토큰이 아닙니다.");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }


            // username, role 값을 획득
            String username = jwtUtil.getUsername(accessToken);
            String role = jwtUtil.getRole(accessToken);

            UserEntity userEntity = UserEntity.builder()
                    .username(username)
                    .role(role)
                    .build();

            CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            log.info("토큰 검증 완료");
            filterChain.doFilter(request, response);
        }

    }

