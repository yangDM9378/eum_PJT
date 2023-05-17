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
public class WebSocketRoomRes {
    Set<String> userNames = new HashSet<>();
    List<StickerRes> stickers = new ArrayList<>();
    String frameUrl;

    @Builder
    public WebSocketRoomRes(String userNames, String frameUrl) {
        this.userNames.add(userNames);
        this.frameUrl = frameUrl;
    }
    public void addUserName(String userName) {
        userNames.add(userName);
    }

    public void deleteUserName(String userName) {
        userNames.remove(userName);
    }
}
