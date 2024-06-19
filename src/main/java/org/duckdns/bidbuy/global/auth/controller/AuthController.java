package org.duckdns.bidbuy.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.dto.EmailCheckReq;
import org.duckdns.bidbuy.app.user.service.EmailService;
import org.duckdns.bidbuy.app.user.service.UserService;
import org.duckdns.bidbuy.global.auth.domain.SignupRequest;
import org.duckdns.bidbuy.global.auth.domain.SignupResponse;
import org.duckdns.bidbuy.global.auth.service.AuthService;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
@Slf4j
public class AuthController {

  private final AuthService authService;
  private final EmailService emailService;
  private final UserService userService;

  @Operation(summary = "사용자 회원가입 API", description = "사용자의 정보를 입력해서 회원가입.")
  @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ApiResponse<SignupResponse>> createUser(
          @RequestPart(value = "userSignupDTO") SignupRequest signupRequest,
          @RequestPart(value = "img", required = false) List<MultipartFile> list) throws IOException {

    User user = authService.createUser(signupRequest,list);
    SignupResponse signupResponse = new SignupResponse(user.getId(),user.getUsername());
    ApiResponse<SignupResponse> response = new ApiResponse<>("201", "정상적으로 회원가입이 완료되었습니다.", signupResponse);

    return ResponseEntity.created(null).body(response);
  }

  @Operation(summary = "이메일 인증코드 발송 API")
  @PostMapping("/emailCheck")
  public ResponseEntity<ApiResponse<String>> EmailCheck(@RequestBody EmailCheckReq emailCheckReq) throws MessagingException, UnsupportedEncodingException {
    String result = authService.findUser(emailCheckReq);
    String authCode = "";
    if(result.equals("notExist")) {
      authCode = emailService.sendEmail(emailCheckReq.getEmail());
    }
    ApiResponse<String> response = new ApiResponse("200", "이메일 인증코드를 발송했습니다.",authCode);

    return ResponseEntity.ok(response);	// Response body에 값을 반환해줄게요~
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
