package org.duckdns.bidbuy.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.global.auth.domain.SignupRequest;
import org.duckdns.bidbuy.global.auth.domain.SignupResponse;
import org.duckdns.bidbuy.global.auth.service.AuthService;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = "application/json", consumes = "application/json")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "사용자 회원가입 API", description = "사용자의 정보를 입력해서 회원가입.")
  @PostMapping
  public ResponseEntity<ApiResponse<SignupResponse>> createUser(@RequestBody SignupRequest signupRequest) {
    User user = authService.createUser(signupRequest);
    SignupResponse signupResponse = new SignupResponse(user.getId(),user.getUsername());
    ApiResponse<SignupResponse> response = new ApiResponse<>("201", "정상적으로 회원가입이 완료되었습니다.", signupResponse);

    return ResponseEntity.created(null).body(response);
  }


//  @PostMapping
//  public ResponseEntity<ApiResponse<SignupResponse>> signup(@RequestBody SignupRequest signupRequest) {
//    User user = authService.createUser(signupRequest);
//
//    SignupResponse signupResponse = new SignupResponse(user.getId(),user.getUsername());
//    ApiResponse<SignupResponse> response = new ApiResponse<>("201", "정상적으로 회원가입이 완료되었습니다.", signupResponse);
//    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
//            .path("/{id}")
//            .buildAndExpand(user.getId())
//            .toUri();
//    return ResponseEntity.created(uri).body(response);
//  }

//  @PostMapping("/login")
//  public Response<ApiResponse<String>> login(@RequestBody LoginRequest loginRequest) {
//    String token = authService.login(loginRequest);
//    ApiResponse<String> response = new ApiResponse<>("200", "로그인에 성공하였습니다.", token);
//    return Response.ok().body(response);
//  }

}
