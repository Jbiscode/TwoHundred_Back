package org.duckdns.bidbuy.global.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long>{
  Boolean existsByRefreshToken(String refreshToken);
  Boolean existsByUserId(Long userId);
  
  @Transactional
  void deleteByRefreshToken(String refreshToken);

  @Transactional
  void deleteByUserId(Long userId);

  RefreshTokenEntity findByUserId(Long userId);
}
