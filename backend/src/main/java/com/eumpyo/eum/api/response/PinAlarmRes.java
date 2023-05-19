package com.eumpyo.eum.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PinAlarmRes {
    // 핀 제목
    String title;

    // 역할
    String role;

    // 그룹
    Long groupId;

    @Builder
    public PinAlarmRes(String title, String role, Long groupId) {
        this.title = title;
        this.role = role;
        this.groupId = groupId;
    }
}
