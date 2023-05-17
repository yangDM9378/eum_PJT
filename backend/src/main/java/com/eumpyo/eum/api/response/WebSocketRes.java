package com.eumpyo.eum.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class WebSocketRes {
    List<String> userNames = new ArrayList<>();
    Long stickerId;
    Double x;
    Double y;
    Double width;
    Double height;
    Double degree;

    @Builder
    public WebSocketRes(Long stickerId, Double x, Double y, Double width, Double height, Double degree) {
        this.stickerId = stickerId;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.degree = degree;
    }

    public void addUserName (String userName) {
        userNames.add(userName);
    }
}
