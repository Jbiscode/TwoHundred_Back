package org.duckdns.bidbuy.global.auth.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String password;
}

