package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.WebSocketReq;
import com.eumpyo.eum.api.response.WebSocketRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
    private static Map<Session, Map<String, String>> rooms = Collections.synchronizedMap(new HashMap<>());
    @OnOpen
    public void onOpen(Session session) throws Exception {
        // 클라이언트를 세션에 저장
        log.info("open session : {}, rooms={}", session.toString(), rooms);

        if(!rooms.containsKey(session)) {
            rooms.put(session, null);
            log.info("session open : {}", session);
        }else{
            log.info("이미 연결된 session");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 방 번호는 방을 만든 사용자 이름 + 사진 번호로 encoding
        WebSocketReq webSocketReq = objectMapper.readValue(message, WebSocketReq.class);
//        log.info(webSocketReq.toString());
        // 방 번호를 받고 방 번호에 해당하는 클라이언트들을 등록 해줘

        if (rooms.get(session) == null) {
            rooms.put(session, new HashMap<>(){{
//                log.info("put user name : {}", webSocketReq.getUserName());
                put("userName", webSocketReq.getUserName());
                put("roomId", webSocketReq.getRoomId());
            }});
        } else {
            Set<Session> sessions = new HashSet<>();
            WebSocketRes webSocketRes = WebSocketRes
                    .builder()
                    .x(webSocketReq.getX())
                    .y(webSocketReq.getY())
                    .build();

            for (Session s : rooms.keySet()) {
                if (rooms.get(s).get("roomId").equals(webSocketReq.getRoomId())) {
                    sessions.add(s);
//                    log.info("add user name : {}", rooms.get(s).get("userName"));
                    webSocketRes.addUserName(rooms.get(s).get("userName"));
                }
            }
            String webSocketResJson = objectMapper.writeValueAsString(webSocketRes);

            for (Session s : sessions) {
//                log.info("send data : x = {}, y = {}", webSocketReq.getX(), webSocketReq.getY());
                s.getBasicRemote().sendText(webSocketResJson);
            }
        }

        // 좌표를 받은 x, y 좌표로 변경해주세요.
//        log.info("receive message : {}", message);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("session close : {}", session);
        rooms.remove(session);
    }
}
