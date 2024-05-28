package org.duckdns.bitwatchu.app.user.service;

import org.duckdns.bitwatchu.app.user.domain.UserEntity;
import org.duckdns.bitwatchu.app.user.repository.UserRepository;
import org.duckdns.bitwatchu.global.auth.domain.SignupRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

}
