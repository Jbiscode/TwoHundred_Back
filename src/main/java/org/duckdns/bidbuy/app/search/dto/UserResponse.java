package org.duckdns.bidbuy.app.search.dto;

import lombok.Getter;
import org.duckdns.bidbuy.app.user.domain.User;

@Getter
public class UserResponse {
    private String addr1;
    private String addr2;

    public UserResponse(User user) {
        addr1 = user.getAddr1();
        addr2 = user.getAddr2();
    }
}
