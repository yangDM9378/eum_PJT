package com.eumpyo.eum.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.handler.TextWebSocketHandler;

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
    private static Map<Session, String> rooms = Collections.synchronizedMap(new HashMap<>());

    @OnOpen
    public void onOpen(Session session) throws Exception {
        // 클라이언트를 세션에 저장
        log.info("open session : {}, rooms={}", session.toString(), rooms);

        if(!rooms.containsKey(session)) {
            rooms.put(session, "");
            log.info("session open : {}", session);
        }else{
            log.info("이미 연결된 session");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // 방 번호는 방을 만든 사용자 이름 + 사진 번호로 encoding

        // 방 번호를 받고 방 번호에 해당하는 클라이언트들을 등록 해줘
        StringTokenizer st = new StringTokenizer(message);

        String roomNo = st.nextToken();

        if (rooms.get(session).equals("")) {
            rooms.put(session, roomNo);
        } else {
            Set<Session> sessions = new HashSet<>();
            for (Session s : rooms.keySet()) {
                if (rooms.get(s).equals(roomNo)) {
                    sessions.add(session);
                }
            }

            for (Session s : sessions) {
                log.info("send data : {}", st.nextToken());
                s.getBasicRemote().sendText(st.nextToken());
            }
        }

        // 좌표를 받은 x, y 좌표로 변경해주세요.
        log.info("receive message : {}", message);


    }

    @OnClose
    public void onClose(Session session) {
        log.info("session close : {}", session);
        rooms.remove(session);
    }
}
