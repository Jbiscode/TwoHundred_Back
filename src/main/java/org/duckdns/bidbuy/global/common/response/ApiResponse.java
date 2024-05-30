package org.duckdns.bidbuy.global.common.response;


public record ApiResponse<T>(
        String resultCode,
        String msg,
        T data)
{ }