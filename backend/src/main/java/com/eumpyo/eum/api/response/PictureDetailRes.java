package com.eumpyo.eum.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PictureDetailRes {
    String image;
    Long userId;
    String userName;
    LocalDateTime createdDate;
    @Builder
    public PictureDetailRes(String image, Long userId, String userName, LocalDateTime createdDate) {
        this.image = image;
        this.userId = userId;
        this.userName = userName;
        this.createdDate = createdDate;
    }
}
