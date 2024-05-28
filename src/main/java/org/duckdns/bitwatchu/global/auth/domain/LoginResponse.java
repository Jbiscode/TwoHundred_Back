package org.duckdns.bitwatchu.global.auth.domain;


public record LoginResponse(
        String role,
        String username,
        String name,
        String email)
{ }