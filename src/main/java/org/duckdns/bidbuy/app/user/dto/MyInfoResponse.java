package org.duckdns.bidbuy.app.user.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoResponse {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String addr1;
    private String addr2;
    private String profileImageUrl;
}
