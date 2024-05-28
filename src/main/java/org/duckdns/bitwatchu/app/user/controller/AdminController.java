package org.duckdns.bitwatchu.app.user.controller;

import org.duckdns.bitwatchu.global.auth.domain.LoginResponse;
import org.duckdns.bitwatchu.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
  @RequestMapping("/admin")
  public String admin() {
    return "admin";
  }

  @GetMapping("/user")
  public ResponseEntity<ApiResponse<LoginResponse>> user() {
    return ResponseEntity.ok(new ApiResponse<>("200", "success", new LoginResponse("USER", "username", "name", "email")));
  }

  @GetMapping("/manager")
  public ResponseEntity<ApiResponse<String>> manager() {
    return ResponseEntity.ok(new ApiResponse<>("200", "success", "manager"));
  }
}
