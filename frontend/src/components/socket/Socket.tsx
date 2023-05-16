"use client";

import React, { useEffect, useRef } from "react";
import { useAppSelector } from "@/redux/hooks";
import { usePathname } from "next/navigation";
const UserName = () => {
  const ws = useRef<null | WebSocket>(); //webSocket을 담는 변수,
  const userName = useAppSelector((state) => state.userReducer.name);
  const pathName = usePathname();
  const decode = decodeURIComponent(pathName.substring(7));
  const openSocket = () => {
    if (ws) {
      ws.current = new WebSocket("ws://localhost:8080/socket/room");

      ws.current.onmessage = (message) => {
        const dataSet = JSON.parse(message.data);
        console.log(dataSet)
    }

      const data = {
        userName:userName,
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
  
  const sendData = () => {
    if (ws?.current?.readyState === 0 ){
      ws.current.onopen = () => {
        console.log(ws.current?.readyState)
      }
    } else {
      const data = {
        roomId: decode,
        x: 1,
        y: 20,
      };
      const temp = JSON.stringify(data);
      ws?.current?.send(temp)
    }
  }

  const roomCode = async () => {
    const code = pathName.substring(7);
    alert("초대 코드가 복사되었습니다." + code);
    if ((window as any).Android) {
      (window as any).Android.copyToClipboard(code);
    } else {
      const clipboardPermission = await navigator.permissions.query({
        name: "clipboard-write" as PermissionName,
      });
      if (clipboardPermission.state === "granted") {
        await navigator.clipboard.writeText(code);
      } else {
      }
    }
  };

  useEffect(() => {
    openSocket();
  }, []);
  return (
    <section>
      <div onClick={roomCode}>초대 코드</div>
      <div onClick={sendData}>클릭하세요</div>
    </section>
  );
};

export default UserName;
