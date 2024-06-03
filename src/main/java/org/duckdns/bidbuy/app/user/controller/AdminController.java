package org.duckdns.bidbuy.app.user.controller;

import org.duckdns.bidbuy.app.user.domain.UserRole;
import org.duckdns.bidbuy.global.auth.domain.LoginResponse;
import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class AdminController {
  @RequestMapping("/admin")
  public String admin() {
    return "admin";
  }

  @GetMapping("/user")
  public ResponseEntity<ApiResponse<LoginResponse>> user() {
    return ResponseEntity.ok(new ApiResponse<>("200", "success", new LoginResponse(1L, UserRole.USER,"username", "name", "email")));
  }

  @GetMapping("/manager")
  public ResponseEntity<ApiResponse<String>> manager() {
    return ResponseEntity.ok(new ApiResponse<>("200", "success", "manager"));
  }
}
