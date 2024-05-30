package org.duckdns.bidbuy.global.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.duckdns.bidbuy.app.user.domain.UserEntity;
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

  public UserEntity createUser(SignupRequest userDTO) {
    Optional<UserEntity> isExist = userRepository.findByUsername(userDTO.getUsername());
    if (isExist.isPresent()) {
      throw new DuplicateIdExistException("이미 존재하는 사용자입니다.");
    }
    UserEntity user = UserEntity.builder()
                                      .username(userDTO.getUsername())
                                      .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                                      .role("USER")
                                      .createdDate(LocalDateTime.now())
                                      .modifiedDate(LocalDateTime.now())
                                      .build();
    return userRepository.save(user);
  }
}
