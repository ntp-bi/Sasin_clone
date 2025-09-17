package com.ntp.sasin_be.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    STAFF;

    @Override
    public String getAuthority() {
        return name();
    }
}
