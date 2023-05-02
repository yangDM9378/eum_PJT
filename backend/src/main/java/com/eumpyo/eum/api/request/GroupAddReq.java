package com.eumpyo.eum.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 그룹 생성
 */
@Getter
public class GroupAddReq {
    // 이름
    String name;

    // 생성일
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdDate;

    // 설명
    String description;

    // 이미지
    String image;
    @Builder
    public GroupAddReq(String name, LocalDateTime createdDate, String description, String image) {
        this.name = name;
        this.createdDate = createdDate;
        this.description = description;
        this.image = image;
    }
}
