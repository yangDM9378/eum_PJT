package com.eumpyo.eum.api.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupDetailsRes {
    // 그룹 ID
    Long groupId;

    // 이름
    String name;

    // 생성일
    LocalDateTime createdDate;

    // 설명
    String description;

    // 이미지
    String image;

    // 그룹 코드
    String groupCode;

    @Builder
    public GroupDetailsRes(Long groupId, String name, LocalDateTime createdDate, String description, String image, String groupCode) {
        this.groupId = groupId;
        this.name = name;
        this.createdDate = createdDate;
        this.description = description;
        this.image = image;
        this.groupCode = groupCode;
    }
}
