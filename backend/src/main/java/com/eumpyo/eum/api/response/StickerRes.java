package com.eumpyo.eum.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StickerRes {
    Long stickerId;
    double x;
    double y;
    double width;
    double height;
    double degree;

    @Builder
    public StickerRes(Long stickerId, double x, double y, double width, double height, double degree) {
        this.stickerId = stickerId;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.degree = degree;
    }
}
