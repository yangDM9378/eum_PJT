package com.eumpyo.eum.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class PicturePinRes {
    Long pictureId;
    String image;
}
