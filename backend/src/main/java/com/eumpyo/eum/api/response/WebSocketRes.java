package com.eumpyo.eum.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
public class WebSocketRes {
    Set<String> userNames = new HashSet<>();
    StickerRes stickerRes;
    String frameUrl;

    @Builder
    public WebSocketRes(StickerRes stickerRes, String frameUrl) {
        this.stickerRes = stickerRes;
        this.frameUrl = frameUrl;
    }

    public void addUserName (String userName) {
        userNames.add(userName);
    }
}
