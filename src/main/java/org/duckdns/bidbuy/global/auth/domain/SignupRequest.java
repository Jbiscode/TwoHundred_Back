package org.duckdns.bidbuy.global.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SignupRequest {
  private String username;
  private String password;
  private String email;

  private String addr1;
  private String addr2;
}
