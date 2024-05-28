package org.duckdns.bitwatchu.global.common.response;


public record ApiResponse<T>(
        String resultCode,
        String msg,
        T data)
{ }