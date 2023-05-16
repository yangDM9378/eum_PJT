package com.eumpyo.eum.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class WebSocketRes {
    Double x;
    Double y;
    List<String> userNames = new ArrayList<>();

    @Builder
    public WebSocketRes(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public void addUserName (String userName) {
        userNames.add(userName);
    }
}
