package org.duckdns.bitwatchu.global.auth.controller;

import org.duckdns.bitwatchu.app.user.domain.UserEntity;
import org.duckdns.bitwatchu.global.auth.domain.LoginRequest;
import org.duckdns.bitwatchu.global.auth.domain.SignupRequest;
import org.duckdns.bitwatchu.global.auth.domain.SignupResponse;
import org.duckdns.bitwatchu.global.auth.service.AuthService;
import org.duckdns.bitwatchu.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = "application/json", consumes = "application/json")
public class AuthController {

  private final AuthService authService;

  @PostMapping
  public ResponseEntity<ApiResponse<SignupResponse>> signup(@RequestBody SignupRequest signupRequest) {
    UserEntity user = authService.createUser(signupRequest);

    SignupResponse signupResponse = new SignupResponse(user.getId());
    ApiResponse<SignupResponse> response = new ApiResponse<>("201", "정상적으로 회원가입이 완료되었습니다.", signupResponse);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                                                      .path("/{id}")
                                                      .buildAndExpand(user.getId())
                                                      .toUri();
    return ResponseEntity.created(uri).body(response);
  }

//  @PostMapping("/login")
//  public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest) {
//    String token = authService.login(loginRequest);
//    ApiResponse<String> response = new ApiResponse<>("200", "로그인에 성공하였습니다.", token);
//    return ResponseEntity.ok().body(response);
//  }

}
