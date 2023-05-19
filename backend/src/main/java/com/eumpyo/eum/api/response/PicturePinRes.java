package com.eumpyo.eum.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PicturePinRes {
    String userName;
    LocalDateTime createdDate;
    Long pictureId;
    String image;
}
