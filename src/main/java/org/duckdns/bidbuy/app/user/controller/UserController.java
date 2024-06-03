package org.duckdns.bidbuy.app.user.controller;


import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.service.UserService;

import org.duckdns.bidbuy.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = "application/json", consumes = "application/json")
public class UserController {

  private final UserService userService;




//  @GetMapping(value = "/my")
//  public ResponseEntity<ApiResponse<MyProfileResponse>> getUser(Authentication authentication) {
//
//      // 인증된 사용자정보에서 user_id 값 받아오기.
//      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//      Long user_id = userDetails.getUser().getId();
//
//      Optional<User> findUser = userService.findById(user_id);
//      if(findUser.isEmpty()) {
//         throw new UserNotFoundException(String.valueOf(user_id));
//      }
//
//      User user = findUser.get();
//
//  }

}
