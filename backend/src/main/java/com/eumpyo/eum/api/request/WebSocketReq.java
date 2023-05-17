package com.eumpyo.eum.api.request;

import com.eumpyo.eum.api.response.StickerRes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;

@ToString
@Setter
@Getter
public class WebSocketReq {
    String userName;
    String roomId;
    StickerReq stickerReq;
    String frameUrl;

}
