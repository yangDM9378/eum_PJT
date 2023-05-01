package com.eumpyo.eum.config.oauth2.userInfo;

import java.util.Map;

public interface OAuth2UserInfo {
    Map<String, Object> getAttributes();
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getAgeRange();
    String getGender();
}
