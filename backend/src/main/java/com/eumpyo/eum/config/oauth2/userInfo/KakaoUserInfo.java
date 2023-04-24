package com.eumpyo.eum.config.oauth2.userInfo;

import lombok.ToString;

import java.util.Map;

@ToString
public class KakaoUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;
    private Map<String, Object> attributesAccount;
    private Map<String, Object> attributesProfile;
    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "Kakao";
    }

    @Override
    public String getEmail() {
        return attributesAccount.get("email").toString();
    }

    @Override
    public String getName() {
        return attributesProfile.get("nickname").toString();
    }

    @Override
    public String getAgeRange() {
        return attributesAccount.get("age_range").toString();
    }

    @Override
    public String getGender() { return attributesAccount.get("gender").toString(); }
}
