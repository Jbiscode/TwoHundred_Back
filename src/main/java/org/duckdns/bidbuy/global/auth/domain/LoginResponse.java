package org.duckdns.bidbuy.global.auth.domain;

import org.duckdns.bidbuy.app.user.domain.UserRole;


public record LoginResponse(
        Long userId,
        UserRole role,
        String username,
        String name,
        String email)
{ }