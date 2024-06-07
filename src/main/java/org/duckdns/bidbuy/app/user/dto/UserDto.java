package org.duckdns.bidbuy.app.user.dto;

import lombok.*;
import org.duckdns.bidbuy.app.user.domain.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private String name;
    private String addr1;
    private String addr2;
    private String provider;
    private String providerId;
    private String profileImageUrl;
    private Integer score;
    private Integer offerLevel;
    private UserRole role;

}
