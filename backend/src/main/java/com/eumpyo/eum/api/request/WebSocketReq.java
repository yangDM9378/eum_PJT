package com.eumpyo.eum.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class WebSocketReq {
    String roomId;
    String userName;
    Long stickerId;
    double x;
    double y;
    double width;
    double height;
    double degree;

}
