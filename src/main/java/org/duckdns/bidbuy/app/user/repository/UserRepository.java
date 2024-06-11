package org.duckdns.bidbuy.app.user.repository;

import java.util.Optional;

import jakarta.annotation.Nullable;
import org.duckdns.bidbuy.app.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


  Optional<User> findById(Long userid);
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);

  Optional<User> findByArticlesId(Long articleId);
}

