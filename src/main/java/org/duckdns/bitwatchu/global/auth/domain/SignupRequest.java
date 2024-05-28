package org.duckdns.bitwatchu.global.auth.domain;

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
  private String name;
  private String email;
}
