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
            WebSocketRes webSocketRes = rooms.getOrDefault(webSocketReq.getRoomId(), null);

            // 방이 존재하지 않으면 방 생성
            if (webSocketRes == null) {
                webSocketRes = WebSocketRes
                        .builder()
                        .frameUrl(webSocketReq.getFrameUrl())
                        .build();

                webSocketRes.addUser(webSocketReq.getUserName());

                // 방에 저장
                rooms.put(webSocketReq.getRoomId(), webSocketRes);

            } else { // 기존에 존재하던 방에 참여
                webSocketRes.addUser(webSocketReq.getUserName());
            }

            // session과 roomId를 매핑 시켜줍니다.
            clients.put(session, new HashMap<>() {{
                put("userName", webSocketReq.getUserName());
                put("roomId", webSocketReq.getRoomId());
            }});

        } else { // 할당된 방에 수정된 좌표를 보여줍니다.
            WebSocketRes room = rooms.get(webSocketReq.getRoomId());
            Set<StickerRes> stickers = room.getStickerRes();
            for (StickerRes sticker : stickers) {
                if (sticker.getStickerId() == webSocketReq.getStickerReq().getStickerId()) {
                    stickers.remove(sticker);
                    break;
                }
            }
            StickerReq stickerReq = webSocketReq.getStickerReq();
            StickerRes stickerRes = StickerRes
                    .builder()
                    .x(stickerReq.getX())
                    .y(stickerReq.getY())
                    .stickerId(stickerReq.getStickerId())
                    .degree(stickerReq.getDegree())
                    .width(stickerReq.getWidth())
                    .height(stickerReq.getHeight())
                    .build();

            stickers.add(stickerRes);
        }

        WebSocketRes webSocketRes = rooms.get(webSocketReq.getRoomId());
        // 생성한 사용자에게 메시지 전송
        String webSocketJson = objectMapper.writeValueAsString(webSocketRes);

        for (Session s : clients.keySet()) {
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
        webSocketRes.deleteUser(clients.get(session).get("userName"));

        // 사용자가 방에 존재하지 않으면 삭제
        if (webSocketRes.getUserNames().size() == 0){
            rooms.remove(roomId);
        }

        // 세션에서 사용자 삭제
        clients.remove(session);
    }
}
