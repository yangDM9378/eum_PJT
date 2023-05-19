package com.eumpyo.eum.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StickerReq {
    Long id;
    int title;
    double x;
    double y;
    double width;
    double height;
    double rotation;

    @Builder
    public StickerReq(Long id, int title, double x, double y, double width, double height, double rotation) {
        this.id= id;
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
}
