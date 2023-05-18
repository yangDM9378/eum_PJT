package com.eumpyo.eum.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StickerRes {
    Long id;
    int title;
    double x;
    double y;
    double width;
    double height;
    double rotation;

    @Builder
    public StickerRes(Long id, int title, double x, double y, double width, double height, double rotation) {
        this.id = id;
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
}
