package org.duckdns.bidbuy.global.auth.controller;

import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;

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
  public ResponseEntity<Void> redirectNaver() {
    String clientId = naverClientId;
    String redirectURI = naverRedirectUri;
    SecureRandom random = new SecureRandom();
    String state = new BigInteger(130, random).toString(32);

    String naverAuthUrl = String.format(
            "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=%s",
            clientId, redirectURI, state
    );

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(naverAuthUrl));
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
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
