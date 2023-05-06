package com.eumpyo.eum.api.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupListRes {
    // 그룹 ID
    Long groupId;

    // 이름
    String name;

    // 설명
    String description;

    // 이미지
    String image;

    @Builder
    public GroupListRes(Long groupId, String name, String description, String image) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
