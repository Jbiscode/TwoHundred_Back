package org.duckdns.bidbuy.app.user.service;

import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;


}
