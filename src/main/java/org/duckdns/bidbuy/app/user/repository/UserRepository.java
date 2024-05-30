package org.duckdns.bidbuy.app.user.repository;

import java.util.Optional;

import org.duckdns.bidbuy.app.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);
}

