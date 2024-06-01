package org.duckdns.bidbuy.global.auth.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/oauth2")
public class OAuthController {
  @Value("${oauth.naver.client-id}")
  private String naverClientId;
  @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
  private String naverRedirectUri;

  @GetMapping("/redirect/naver")
  public void redirectNaver(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String redirectUrl = "https://api-bidbuy.duckdns.org/oauth2/authorization/naver";

    // CORS 헤더 설정
    response.setHeader("Access-Control-Allow-Origin", "https://bidbuy.duckdns.org");
    response.setHeader("Access-Control-Allow-Credentials", "true");

    // 기존 요청 파라미터 추가
    String queryString = request.getQueryString();
    if (queryString != null) {
      redirectUrl += "?" + queryString;
    }

    response.sendRedirect(redirectUrl);
  }

  @GetMapping("/redirect/kakao")
  public ResponseEntity<Void> redirectKakao() {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("https://api-bidbuy.duckdns.org/oauth2/authorization/kakao"));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  @GetMapping("/redirect/google")
  public ResponseEntity<Void> redirectGoogle() {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create("https://api-bidbuy.duckdns.org/oauth2/authorization/google"));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }
}
