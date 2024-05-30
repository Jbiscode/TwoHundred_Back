package org.duckdns.bidbuy.app.user.repository;

import java.util.Optional;

import org.duckdns.bidbuy.app.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsername(String username);
}

