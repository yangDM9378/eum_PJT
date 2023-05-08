package com.eumpyo.eum.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PinListRes {
    Long pinId;

    Double latitude;

    Double longitude;

    @Builder
    public PinListRes(Long pinId, Double latitude, Double longitude) {
        this.pinId = pinId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
