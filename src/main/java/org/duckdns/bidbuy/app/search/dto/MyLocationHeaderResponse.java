package org.duckdns.bidbuy.app.search.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MyLocationHeaderResponse {
    private Long totalCount;
    private List<UserResponse> address;

    public MyLocationHeaderResponse(Long totalCount,List<UserResponse> address ) {
        this.totalCount = totalCount;
        this.address = address;
    }
}
