package com.eumpyo.eum.api.request;

import lombok.Builder;
import lombok.Getter;

/**
 * 그룹 생성
 */
@Getter
public class GroupAddReq {
    // 이름
    String name;

    // 설명
    String description;

    // 그룹 이미지
    String image;

    @Builder
    public GroupAddReq(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
