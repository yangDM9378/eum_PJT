package com.eumpyo.eum.api.request;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 그룹 생성
 */
@Getter
public class GroupAddReq {
    // 이름
    String name;

    // 생성일
    LocalDateTime createdDate;

    // 설명
    String description;

    // 이미지
    String image;
}
