package com.eumpyo.eum.api.response;

import com.eumpyo.eum.db.entity.Group;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PinDetailsRes {
    // 핀 제목
    String title;

    // 핀 내용
    String content;

    // 핀 이미지
    String image;

    // 생성일
    LocalDateTime createdDate;

    // 작성자 이름
    String userName;

    // 타입
    String type;

    @Builder
    public PinDetailsRes(String title, String content, String image, LocalDateTime createdDate, String userName, String type) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.createdDate = createdDate;
        this.userName = userName;
        this.type = type;
    }
}
