"use client";

import React, { useEffect, useRef } from "react";
import { useAppSelector } from "@/redux/hooks";

const UserName = () => {
  const ws = useRef<null | WebSocket>(); //webSocket을 담는 변수,

  const userName = useAppSelector((state) => state.userReducer.name);
  const openSocket = () => {
    if (ws) {
      ws.current = new WebSocket("ws://localhost:8080/socket/room");

      const data = {
        roomId: 96,
        x: 25,
        y: 47,
      };

      const temp = JSON.stringify(data);

      ws.current.onopen = () => {
        if (ws.current) {
          ws.current.send(temp);
        }
      };
    }
  };

  useEffect(() => {
    openSocket();
  }, []);
  return <div>{userName}</div>;
};

export default UserName;
