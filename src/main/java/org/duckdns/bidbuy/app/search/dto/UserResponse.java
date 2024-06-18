package org.duckdns.bidbuy.app.search.dto;

import lombok.Getter;

@Getter
public class UserResponse {
    private Long id;
    private String addr1;
    private String addr2;

    public UserResponse(Long id, String addr1, String addr2) {
        this.id = id;
        this.addr1 = addr1;
        this.addr2 = addr2;
    }
}
