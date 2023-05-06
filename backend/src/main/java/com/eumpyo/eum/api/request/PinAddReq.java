package com.eumpyo.eum.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 핀 생성
 */
@Getter
@Setter
public class PinAddReq {
    // 제목
    String title;

    // 내용
    String content;

    // 위도
    Double latitude;

    // 경도
    Double longitude;

    // 타입
    String type;

    // 그룹id
    Long groupId;

    @Builder
    public PinAddReq(String title, String content, Double latitude, Double longitude, String type, Long groupId) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.groupId = groupId;
    }
}
