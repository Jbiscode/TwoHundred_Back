package org.duckdns.bidbuy.app.user.controller;


import org.duckdns.bidbuy.app.user.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = "application/json", consumes = "application/json")
public class UserController {

  private final UserService userService;


}
