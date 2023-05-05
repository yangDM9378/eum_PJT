package com.eumpyo.eum.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PinAlarmRes {
    // 핀 제목
    String title;

    // 역할
    String role;

    @Builder
    public PinAlarmRes(String title, String role) {
        this.title = title;
        this.role = role;
    }
}
