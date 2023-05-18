"use client";
// 스티커 컴포넌트

import { useAppSelector } from "@/redux/hooks";
import React, { useEffect, useState, useRef } from "react";
import { Stage, Layer, Image, Group, Transformer } from "react-konva";
import { usePathname } from "next/navigation";
import FrameImgChild from "./frameImgChild";
interface StickerRes {
  stickerId: number;
  x: number;
  y: number;
  width: number;
  height: number;
  degree: number;
}

interface WebSocketRes {
  roomId: string;
  userNames: string[];
  stickers: StickerRes[];
  frameUrl: string;
}

const FrameImg = () => {
  const ws = useRef<null | WebSocket>(); //webSocket을 담는 변수,
  const userName = useAppSelector((state) => state.userReducer.name);
  const [socketData, setSocketData] = useState<WebSocketRes>();

  const openSocket = () => {
    if (ws) {
      ws.current = new WebSocket("ws://localhost:8080/socket/room");
      ws.current.onmessage = (message) => {
        const dataSet: WebSocketRes = JSON.parse(message.data);
        console.log("data", dataSet);
        setSocketData(dataSet);
      };

      const stickerData = {
        stickerId: null,
        x: null,
        y: null,
        width: null,
        height: null,
        degree: null,
      };
      // 방만들기 요청 데이터
      const data = {
        roomId: decoCode,
        userName: userName,
        frameUrl:
          "https://webisfree.com/static/uploads/2019/6639_images194.jpg",
      };
      const temp = JSON.stringify(data);
      console.log(temp);
      ws.current.onopen = () => {
        if (ws.current) {
          ws.current.send(temp);
        }
      };
    } else {
      console.log("없어요");
    }
  };

  const sendData = async () => {
    const stickerData = {
      stickerId: null,
      x: null,
      y: null,
      width: null,
      height: null,
      degree: null,
    };

    // 움직이는 시간
    const data = {
      roomId: decoCode,
      userName: userName,
      stickerReq: stickerData,
      frameUrl: "https://webisfree.com/static/uploads/2019/6639_images194.jpg",
    };

    const temp = JSON.stringify(data);
    if (ws?.current?.readyState === 0) {
      ws.current.onopen = () => {
        console.log(ws.current?.readyState);
        ws?.current?.send(temp);
      };
    } else {
      ws?.current?.send(temp);
    }
  };

  useEffect(() => {
    openSocket();
  }, []);

  // 꾸미기 방 초대 코드
  const path = usePathname();
  const decoCode = path.substring(1, path.length - 20);

  const roomCode = async () => {
    alert("초대 코드가 복사되었습니다." + decoCode);
    if ((window as any).Android) {
      (window as any).Android.copyToClipboard(decoCode);
    } else {
      const clipboardPermission = await navigator.permissions.query({
        name: "clipboard-write" as PermissionName,
      });
      if (clipboardPermission.state === "granted") {
        await navigator.clipboard.writeText(decoCode);
      } else {
        console.log("");
      }
    }
  };
  // 스티커 꾸미기 함수

  const [originImg, setOriginImg] = useState<CanvasImageSource | undefined>(
    undefined
  );
  const stageRef = useRef(null);

  const bgImg = useAppSelector((state) => state.coordsReducer.frameImg);
  useEffect(() => {
    if (bgImg) {
      const img = new window.Image();
      img.src = bgImg;
      img.onload = () => {
        setOriginImg(img);
      };
    }
  }, [bgImg]);

  // 원본 아이콘
  const initialicons = [1, 2, 3, 4, 5, 6, 7];

  // 상태를 변화할
  const [icons, setIcons] = useState<
    {
      id: number;
      src: CanvasImageSource;
      x: number;
      y: number;
      width: number;
      height: number;
      rotation: number;
    }[]
  >([]);

  const [selectedId, setSelectedId] = useState<null | number>(0);
  const [nextImageId, setNextImageId] = useState(0); // 초기 이미지 ID

  // 선택한거 취소하게 하는함수
  const checkDeselect = (e: any) => {
    // 빈 영역 선택하면 id값을 null로
    const clickedOnEmpty = e.target === e.target.getStage();
    if (clickedOnEmpty) {
      setSelectedId(null);
    }
  };

  useEffect(() => {
    console.log(selectedId);
  }, [selectedId]);

  // 아이콘 업데이트 하는 함수
  const handleChange = (title: number) => {
    const newIcon = new window.Image();
    newIcon.src = `/icons/${title}.png`;
    newIcon.onload = () => {
      const newKonvaImage = new window.Image();
      newKonvaImage.src = newIcon.src;
      newKonvaImage.onload = () => {
        const iconId = nextImageId; // 새로운 이미지 ID 할당
        setNextImageId(nextImageId + 1); // 다음 이미지 ID 업데이트

        // 변형할 아이콘들을 추가해주는 작업
        const updatedIcons = [
          ...icons,
          {
            id: iconId,
            src: newIcon,
            x: 0,
            y: 0,
            width: 120,
            height: 120,
            rotation: 0,
          },
        ];
        setIcons(updatedIcons);
      };
    };
  };

  return (
    <div className="h-[92vh] flex flex-col items-center justify-center">
      <div className="flex w-[90%] pb-4 gap-2">
        {socketData &&
          socketData.userNames.map((name, idx) => {
            return (
              <div
                key={idx}
                className="flex flex-col items-center justify-center"
              >
                <div className="rounded-[50%] overflow-hidden w-[8vh] h-[8vh]">
                  <img
                    src="https://i.pinimg.com/564x/c5/c0/50/c5c050a124eff3c5656822db9abddd8c.jpg"
                    alt=""
                    className="w-[100%] h-[100%] "
                  />
                </div>
                <div>{name}</div>
              </div>
            );
          })}
      </div>
      {/* 캔버스 */}
      <Stage
        width={300}
        height={350}
        ref={stageRef}
        onMouseDown={checkDeselect}
        onTouchStart={checkDeselect}
      >
        <Layer>
          {originImg && (
            <Image
              image={originImg}
              alt="frameImg"
              width={300}
              height={350}
              draggable={false}
              onMouseDown={checkDeselect}
              onTouchStart={checkDeselect}
            />
          )}
          {icons?.map((icon, i) => (
            <FrameImgChild
              key={i}
              shapeProps={icon}
              onSelect={() => {
                setSelectedId(icon.id);
              }}
              isSelected={icon.id === selectedId}
              onChange={(newAttrs) => {
                const newicons = icons.slice();
                newicons[i] = newAttrs;
                setIcons(newicons);
              }}
            />
          ))}
        </Layer>
      </Stage>
      {/* 아이콘들 보여주기*/}
      <div className="flex mt-[5%]">
        {initialicons.map((iconName, idx) => {
          return (
            <img
              key={idx}
              className=""
              src={`/icons/${iconName}.png`}
              alt=""
              width={50}
              height={100}
              // 클릭하면 icosn 업데이트 해주는 함수 호출
              onClick={() => handleChange(iconName)}
            />
          );
        })}
      </div>
    </div>
  );
};

export default FrameImg;
