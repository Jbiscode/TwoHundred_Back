package org.duckdns.bidbuy.global.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.domain.UserRole;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.SignupRequest;
import org.duckdns.bidbuy.global.auth.exception.DuplicateIdExistException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public User createUser(SignupRequest userDTO) {
    Optional<User> isExist = userRepository.findByEmail(userDTO.getEmail());

    if (isExist.isPresent()) {
      throw new DuplicateIdExistException("이미 존재하는 사용자입니다.");
    }


    User user = User.builder()
                                      .username(userDTO.getUsername())
                                      .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                                      .role(UserRole.valueOf("USER"))
                                      .createdDate(LocalDateTime.now())
                                      .modifiedDate(LocalDateTime.now())
                                      .build();
    return userRepository.save(user);
  }
}
