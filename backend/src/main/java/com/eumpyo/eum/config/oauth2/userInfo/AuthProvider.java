package com.eumpyo.eum.config.oauth2.userInfo;

import lombok.Getter;

@Getter
public enum AuthProvider {
    KAKAO("kakao");
    private final String providerName;
    AuthProvider(String providerName) {
        this.providerName = providerName;
    }
}
