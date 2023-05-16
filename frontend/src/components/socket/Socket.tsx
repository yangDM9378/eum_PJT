"use client";

import React, { useEffect, useRef } from "react";
import { useAppSelector } from "@/redux/hooks";
import { usePathname } from "next/navigation";
const UserName = () => {
  const ws = useRef<null | WebSocket>(); //webSocket을 담는 변수,
  const userName = useAppSelector((state) => state.userReducer.name);
  const pathName = usePathname();
  const decode = decodeURIComponent(pathName.substring(7));
  console.log(decode);
  const openSocket = () => {
    if (ws) {
      ws.current = new WebSocket("ws://localhost:8080/socket/room");
      const data = {
        roomId: decode,
        x: 25,
        y: 47,
      };
      const temp = JSON.stringify(data);
      console.log(temp);
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
