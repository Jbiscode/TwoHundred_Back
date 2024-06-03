package org.duckdns.bidbuy.global.auth.service;

import java.util.Optional;

import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> userData = userRepository.findByEmail(email);
        // 유저가 존재한다면 반환하고 없으면 null 반환
        return userData.map(CustomUserDetails::new).orElse(null);


    }
}