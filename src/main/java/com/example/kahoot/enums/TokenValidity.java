package com.example.kahoot.enums;

public enum TokenValidity {
    ACCESS_TOKEN_VALIDITY(30 * 1000), // 5 хвилин
    REFRESH_TOKEN_VALIDITY(7 * 24 * 60 * 60 * 1000); // 7 днів

    private final long validity;

    TokenValidity(long validity) {
        this.validity = validity;
    }

    public long getValidity() {
        return validity;
    }
}
