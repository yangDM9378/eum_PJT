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
    Set<StickerRes> stickerRes = new HashSet<>();
    String frameUrl;

    @Builder
    public WebSocketRes(String frameUrl) {
        this.frameUrl = frameUrl;
    }

    public void addSticker(StickerRes stickerRes) {
        this.stickerRes.add(stickerRes);
    }

    public void addUser(String userName) {
        this.userNames.add(userName);
    }

    public void deleteUser(String userName) {
        this.userNames.remove(userName);
    }
}
