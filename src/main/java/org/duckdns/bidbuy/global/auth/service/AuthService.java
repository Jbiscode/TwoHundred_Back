package org.duckdns.bidbuy.global.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.domain.UserRole;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.SignupRequest;
import org.duckdns.bidbuy.global.auth.exception.DuplicateIdExistException;
import org.duckdns.bidbuy.global.error.NullInputException;
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
    if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
      throw new NullInputException("이메일을 입력해주세요.");
    }


    User user = User.builder()
                                      .email(userDTO.getEmail())
                                      .username(userDTO.getUsername())
                                      .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                                      .role(UserRole.USER)
                                      .addr1(userDTO.getAddr1())
                                      .addr2(userDTO.getAddr2())
                                      .createdDate(LocalDateTime.now())
                                      .modifiedDate(LocalDateTime.now())
                                      .build();
    return userRepository.save(user);
  }
}
