package com.eumpyo.eum.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PinListRes {
    Long pinId;

    Double latitude;

    Double longitude;

    String type;

    @Builder
    public PinListRes(Long pinId, Double latitude, Double longitude, String type) {
        this.pinId = pinId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }
}
