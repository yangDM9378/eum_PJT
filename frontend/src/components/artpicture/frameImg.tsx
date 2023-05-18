"use client";
// 스티커 컴포넌트

import { useAppSelector } from "@/redux/hooks";
import React, { useEffect, useState, useRef } from "react";
import { Stage, Layer, Image, Group, Transformer } from "react-konva";
import { usePathname, useRouter } from "next/navigation";
import FrameImgChild from "./frameImgChild";
import Konva from "konva";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { pictureEventApi } from "@/services/eventApi";
interface StickerRes {
  id: number;
  x: number;
  y: number;
  width: number;
  height: number;
  rotation: number;
  title: number;
}

interface WebSocketRes {
  roomId: string;
  userNames: string[];
  stickerRes: StickerRes[];
  frameUrl: string;
}

const FrameImg = () => {
  const ws = useRef<null | WebSocket>(); //webSocket을 담는 변수,
  const userName = useAppSelector((state) => state.userReducer.name);
  const [socketData, setSocketData] = useState<WebSocketRes>();
  const bgImg = useAppSelector((state) => state.coordsReducer.frameImg);
  const openSocket = () => {
    if (ws) {
      ws.current = new WebSocket(`${process.env.NEXT_PUBLIC_SOCKET}`);
      ws.current.onmessage = (message) => {
        const dataSet: WebSocketRes = JSON.parse(message.data);
        // console.log("소켓에서 받은 데이터입니다.", dataSet);
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
        frameUrl: bgImg,
      };
      const temp = JSON.stringify(data);
      console.log(temp);
      ws.current.onopen = () => {
        if (ws.current) {
          ws.current.send(temp);
        }
      };

      ws.current.onclose = (e) => {
        // connection closed
        console.log(e.code, e.reason);
      };
    } else {
      console.log("없어요");
    }
  };
  // 소켓에 움직이는 스티커 데이터를 넘겨줍니다.
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const sendData = async (newAttrs: {
    id: number;
    src: CanvasImageSource;
    x: number;
    y: number;
    width: number;
    height: number;
    rotation: number;
    title: number;
  }) => {
    const stickerData = {
      id: newAttrs.id,
      x: newAttrs.x,
      y: newAttrs.y,
      width: newAttrs.width,
      height: newAttrs.height,
      rotation: newAttrs.rotation,
      title: newAttrs.title,
    };

    // 움직이는 시간
    const data = {
      roomId: decoCode,
      userName: userName,
      stickerReq: stickerData,
      frameUrl: bgImg,
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
    alert("초대 코드가 복사되었습니다.");
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
  const stageRef = useRef<Konva.Stage | null>(null);

  useEffect(() => {
    if (bgImg) {
      const img = new window.Image();
      img.crossOrigin = "anonymous";
      img.src = bgImg;
      img.onload = () => {
        setOriginImg(img);
      };
    }
  }, [bgImg]);

  // 소켓 통신으로 받아온 데이터를 렌더링 합니다.
  useEffect(() => {
    if (socketData) {
      const img = new window.Image();
      img.crossOrigin = "anonymous";
      img.src = socketData.frameUrl;
      img.onload = () => {
        setOriginImg(img);
      };

      const socketIconArr = socketData.stickerRes;
      const newArr = socketIconArr.map((icon) => {
        if (icon.title === -5) {
          router.replace(`/map/${groupId}`);
        }
        // console.log(icon);
        const newIcon = new window.Image();
        newIcon.crossOrigin = "anonymous";
        newIcon.src = `/icons/${icon.title}.png`;
        return { ...icon, src: newIcon };
      });
      const iconArr = newArr.sort((a, b) => a.id - b.id);
      // console.log("새로운 리스트", iconArr);
      setIcons(iconArr);
    }
  }, [socketData]);

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
      title: number;
    }[]
  >([]);

  const [selectedId, setSelectedId] = useState<null | number>(0);
  const [nextImageId, setNextImageId] = useState(0); // 초기 이미지 ID

  // 선택한거 취소하게 하는함수
  const checkDeselect = (e: any) => {
    // 빈 영역 선택하면 id값을 null로
    const clickedOnEmpty =
      e.target === e.target.getStage() || e.target.attrs.id === "background";
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
    newIcon.crossOrigin = "anonymous";
    newIcon.src = `/icons/${title}.png`;
    newIcon.onload = () => {
      const newKonvaImage = new window.Image();
      newKonvaImage.crossOrigin = "anonymous";
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
            title: title,
          },
        ];
        setIcons(updatedIcons);
      };
    };
  };

  //사진 저장
  const queryClient = useQueryClient();
  const EventMutation = useMutation(pictureEventApi, {
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["initial-pinpicture"] });
    },
  });
  const router = useRouter();
  const groupId = useAppSelector((state) => state.coordsReducer.groupId);
  const pinId = useAppSelector((state) => state.coordsReducer.pinId);
  const saveeImg = async () => {
    setSelectedId(null);
    if (stageRef.current) {
      const dataURL = await stageRef.current.toDataURL({ pixelRatio: 1 });
      const jsonReq = { groupId: groupId, pinId: pinId };
      const blobRes = await (await fetch(dataURL)).blob();

      const formData = new FormData();
      formData.append("image", blobRes, "image.png");
      formData.append(
        "pictureAddReq",
        new Blob([JSON.stringify(jsonReq)], { type: "application/json" })
      );
      await EventMutation.mutate(formData);
      await router.replace(`/map/${groupId}`);
    }
    if (socketData) {
      const img = new window.Image();
      img.crossOrigin = "anonymous";
      img.src = socketData.frameUrl;
      // 종료조건
      const endArr = {
        id: socketData?.stickerRes[0].id,
        x: socketData?.stickerRes[0].x,
        y: socketData?.stickerRes[0].y,
        width: socketData?.stickerRes[0].width,
        height: socketData?.stickerRes[0].height,
        rotation: socketData?.stickerRes[0].rotation,
        title: -5,
        src: img,
      };
      sendData(endArr);
    }
  };

  return (
    <div className="h-[92vh] flex flex-col items-center justify-center">
      <div className="w-[90%] flex justify-end">
        <div
          onClick={roomCode}
          className="p-2 text-white rounded-lg bg-brand-red"
        >
          초대 코드
        </div>
      </div>
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
              onClick={checkDeselect}
              onMouseDown={checkDeselect}
              onTouchStart={checkDeselect}
              id="background"
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
                sendData(newAttrs);
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
      {decoCode.includes(userName) ? (
        <button
          onClick={saveeImg}
          className="my-[2vh] bg-brand-blue text-white py-[1.5vh] px-[6vw] rounded-md shadow-xl font-brand-gmarketsans"
        >
          사진저장
        </button>
      ) : (
        <button className="my-[2vh] bg-brand-blue text-white py-[1.5vh] px-[6vw] rounded-md shadow-xl font-brand-gmarketsans">
          사진저장
        </button>
      )}
    </div>
  );
};

export default FrameImg;
