package org.duckdns.bidbuy.app.user.controller;


import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = "application/json", consumes = "application/json")
public class UserController {

  private final UserService userService;

  @GetMapping(value = "/my")
  public User getUser(Authentication authentication) {
      Long userId = Long.valueOf(authentication.getName());

  }

}
