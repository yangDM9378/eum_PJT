package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.WebSocketReq;
import com.eumpyo.eum.api.response.WebSocketRoomRes;
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
    // 포함 되어야 할 정보 Session, Map<userName, RoomId>
    private static Map<Session, Map<String, String>> clients = Collections.synchronizedMap(new HashMap<>());
    private static Map<String, WebSocketRoomRes> rooms = Collections.synchronizedMap(new HashMap<>());

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
        if (clients.get(session) == null) {
            WebSocketRoomRes webSocketRoomRes = rooms.getOrDefault(webSocketReq.getRoomId(), null);
            // 기존에 방이 존재 하지 않을 때
            if (webSocketRoomRes == null) {
                webSocketRoomRes = WebSocketRoomRes.builder()
                        .userNames(webSocketReq.getUserName())
                        .frameUrl(webSocketReq.getFrameUrl())
                        .build();

                log.info("null {}", webSocketRoomRes.toString());
                // 방 생성
                rooms.put(webSocketReq.getRoomId(), webSocketRoomRes);

                // 고객에게 방 할당
                clients.put(session, new HashMap<>() {{
                    put("roomId", webSocketReq.getRoomId());
                    put("userName", webSocketReq.getUserName());
                }});
            }
            // 방이 존재하면
            else {
                if (!webSocketReq.getUserName().equals("")){
                    // 해당 방에 사용자 추가
                    webSocketRoomRes.addUserName(webSocketReq.getUserName());
                }

                // 방을 만든 사용자가 아니기 때문에 그동안 이동된 사진에 대한 정보 같이 전송
                String webSocketResJson = objectMapper.writeValueAsString(webSocketRoomRes);
                log.info("else {}", webSocketResJson);
                session.getBasicRemote().sendText(webSocketResJson);
            }
        }
//        else {
//            Set<Session> sessions = new HashSet<>();
//            StickerRes stickerRes = StickerRes.builder()
//                .x(webSocketReq.getX())
//                .y(webSocketReq.getY())
//                .stickerId(webSocketReq.getStickerId())
//                .width(webSocketReq.getWidth())
//                .height(webSocketReq.getHeight())
//                .degree(webSocketReq.getDegree())
//                .build();
//            WebSocketRes webSocketRes = WebSocketRes
//                    .builder()
//                    .stickerRes(stickerRes)
//                    .frameUrl(webSocketReq.getFrameUrl())
//                    .build();

//            for (Session s : rooms.keySet()) {
//                if (rooms.get(s).get("roomId").equals(webSocketReq.getRoomId())) {
//                    sessions.add(s);
////                    log.info("add user name : {}", rooms.get(s).get("userName"));
//                    webSocketRes.addUserName(rooms.get(s).get("userName"));
//                }
//            }
//            String webSocketResJson = objectMapper.writeValueAsString(webSocketRes);
//
//            for (Session s : sessions) {
////                log.info("send data : x = {}, y = {}", webSocketReq.getX(), webSocketReq.getY());
//                s.getBasicRemote().sendText(webSocketResJson);
//            }
//        }

        // 좌표를 받은 x, y 좌표로 변경해주세요.
//        log.info("receive message : {}", message);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("session close : {}", session);
//
//        // userName, roomId
//        Map<String, String> clientInfo = clients.get(session);
//
//        // 방에서 유저를 삭제
//        WebSocketRoomRes webSocketRoomRes = rooms.get(clientInfo.get("roomId"));
//        webSocketRoomRes.deleteUserName(clientInfo.get("userName"));
//
//        // 방에 유저가 없으면 방을 삭제합니다.
//        if (webSocketRoomRes.getUserNames().size() == 0) {
//            rooms.remove(clientInfo.get("roomId"));
//        }

        // 세션에서 사용자 삭제
        clients.remove(session);
    }
}
