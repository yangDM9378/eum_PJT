package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.StickerReq;
import com.eumpyo.eum.api.request.WebSocketReq;
import com.eumpyo.eum.api.response.StickerRes;
import com.eumpyo.eum.api.response.WebSocketRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@ServerEndpoint("/socket/room")
public class WebSocket {
    // 포함 되어야 할 정보 Session, userName, roomId
    private static Map<Session, Map<String, String>> clients = Collections.synchronizedMap(new HashMap<>());
    private static Map<String, WebSocketRes> rooms = Collections.synchronizedMap(new HashMap<>());

    @OnOpen
    public void onOpen(Session session) throws Exception {
        // 클라이언트를 세션에 저장
        log.info("open session : {}, rooms={}", session.toString(), clients);

        if(!clients.containsKey(session)) {
            clients.put(session, null);
            log.info("session open : {}", session);
        }else{
            log.info("이미 연결된 session");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info(message);
        // 방 번호는 방을 만든 사용자 이름 + 사진 번호로 encoding
        WebSocketReq webSocketReq = objectMapper.readValue(message, WebSocketReq.class);
        log.info(webSocketReq.toString());

        // 방 번호를 받고 방 번호에 해당하는 클라이언트들을 등록 해줘

        // Null이면 사용자에게 방이 할당되어 있지 않음
        if (clients.get(session) == null && !webSocketReq.getUserName().equals("")) {
            log.info("사용자에게 방을 할당 합니다.");
            WebSocketRes webSocketRes = rooms.getOrDefault(webSocketReq.getRoomId(), null);

            // 방이 존재하지 않으면 방 생성
            if (webSocketRes == null) {
                log.info("방을 생성합니다.");
                webSocketRes = WebSocketRes
                        .builder()
                        .frameUrl(webSocketReq.getFrameUrl())
                        .build();

                webSocketRes.addUser(webSocketReq.getUserName());

                // 방에 저장
                rooms.put(webSocketReq.getRoomId(), webSocketRes);

            } else { // 기존에 존재하던 방에 참여
                log.info("기존의 방에 참여 합니다.");
                webSocketRes.addUser(webSocketReq.getUserName());
            }

            log.info("사용자와 방을 매핑 시켜줍니다.");
            // session과 roomId를 매핑 시켜줍니다.
            clients.put(session, new HashMap<>() {{
                put("userName", webSocketReq.getUserName());
                put("roomId", webSocketReq.getRoomId());
            }});

        } else { // 할당된 방에 수정된 좌표를 보여줍니다.
            log.info("좌표를 수정합니다.");
            WebSocketRes room = rooms.get(webSocketReq.getRoomId());
            Set<StickerRes> stickers = room.getStickerRes();
            for (StickerRes sticker : stickers) {
                if (sticker.getId() == webSocketReq.getStickerReq().getId()) {
                    stickers.remove(sticker);
                    break;
                }
            }

            log.info("새로운 스티커를 만듭니다.");
            StickerReq stickerReq = webSocketReq.getStickerReq();
            StickerRes stickerRes = StickerRes
                    .builder()
                    .id(stickerReq.getId())
                    .title(stickerReq.getTitle())
                    .x(stickerReq.getX())
                    .y(stickerReq.getY())
                    .rotation(stickerReq.getRotation())
                    .width(stickerReq.getWidth())
                    .height(stickerReq.getHeight())
                    .build();

            stickers.add(stickerRes);
        }

        log.info("전송할 메시지를 가져옵니다.");
        WebSocketRes webSocketRes = rooms.get(webSocketReq.getRoomId());
        // 생성한 사용자에게 메시지 전송
        String webSocketJson = objectMapper.writeValueAsString(webSocketRes);

        for (Session s : clients.keySet()) {
            log.info(clients.get(s).get("roomId"));
            log.info(webSocketReq.getRoomId());
            if (clients.get(s).get("roomId").equals(webSocketReq.getRoomId())) {
                s.getBasicRemote().sendText(webSocketJson);
            }
        }
//         좌표를 받은 x, y 좌표로 변경해주세요.
        log.info("receive message : {}", message);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("session close : {}", session);
        String roomId = clients.get(session).get("roomId");
        WebSocketRes webSocketRes = rooms.get(roomId);
        if (webSocketRes.getUserNames().contains(clients.get(session).get("userName"))) {
            webSocketRes.deleteUser(clients.get(session).get("userName"));
        }
//
//        // 사용자가 방에 존재하지 않으면 삭제
        if (webSocketRes.getUserNames().size() == 0){
            rooms.remove(roomId);
        }

        // 세션에서 사용자 삭제
        clients.remove(session);
    }
}
