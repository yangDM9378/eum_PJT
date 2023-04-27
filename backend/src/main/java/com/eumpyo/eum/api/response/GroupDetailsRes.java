package com.eumpyo.eum.api.response;

import lombok.Builder;

import java.time.LocalDateTime;

public class GroupDetailsRes {
    // 이름
    String name;

    // 생성일
    LocalDateTime createdDate;

    // 설명
    String description;

    // 이미지
    String image;

    @Builder
    public GroupDetailsRes(String name, LocalDateTime createdDate, String description, String image) {
        this.name = name;
        this.createdDate = createdDate;
        this.description = description;
        this.image = image;
    }
}
