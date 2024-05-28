package org.duckdns.bitwatchu.global.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long>{
  Boolean existsByRefreshToken(String refreshToken);
  
  @Transactional
  void deleteByRefreshToken(String refreshToken);
}
