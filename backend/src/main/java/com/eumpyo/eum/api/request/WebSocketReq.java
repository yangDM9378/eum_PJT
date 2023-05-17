package com.eumpyo.eum.api.request;

import com.eumpyo.eum.api.response.StickerRes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class WebSocketReq {
    String roomId;
    String userName;
    StickerRes stickerRes;
    String frameUrl;

}
