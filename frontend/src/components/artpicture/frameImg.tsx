"use client";
// 스티커 컴포넌트

import { useAppSelector } from "@/redux/hooks";
import React, { useEffect, useState, useRef } from "react";
import { Stage, Layer, Image, Group, Transformer } from "react-konva";
import { usePathname } from "next/navigation";
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
  const frameUrl = useAppSelector((state) => state.coordsReducer.frameImg);
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

  const [originImg, setOriginImg] = useState<CanvasImageSource | undefined>(
    undefined
  );
  const stageRef = useRef(null);
  const transformerRef = useRef(null);
  const [images, setImages] = useState<
    {
      id: number;
      src: CanvasImageSource;
      x: number;
      y: number;
      width: number;
      height: number;
    }[]
  >([]);
  const [nextImageId, setNextImageId] = useState(1); // 초기 이미지 ID
  const [selectedImageId, setSelectedImageId] = useState<number | null>(null); // 선택된 이미지 ID
  const [transformers, setTransformers] = useState<number[]>([]);

  useEffect(() => {
    if (frameUrl) {
      const img = new window.Image();
      img.src = frameUrl;
      img.onload = () => {
        setOriginImg(img);
      };
    }
  }, [frameUrl]);

  const assets = [1, 2, 3, 4];

  const handleImageClick = (imageName: number) => {
    const newImage = new window.Image();
    newImage.src = `/frame/${imageName}.png`;
    newImage.onload = () => {
      const newKonvaImage = new window.Image();
      newKonvaImage.src = newImage.src;
      newKonvaImage.onload = () => {
        const imageId = nextImageId; // 새로운 이미지 ID 할당
        setNextImageId((prevId) => prevId + 1); // 다음 이미지 ID 업데이트

        const updatedImages = [
          ...images,
          {
            id: imageId,
            src: newKonvaImage,
            x: 0, // 이미지의 x 좌표 설정
            y: 0, // 이미지의 y 좌표 설정
            width: 120,
            height: 140,
          },
        ];
        setImages(updatedImages);
      };
    };
  };

  const handleSelectImage = (imageId: number) => {
    setSelectedImageId(imageId);
    if (!transformers.includes(imageId)) {
      setTransformers([...transformers, imageId]);
    }
  };

  const handleDeselectImage = (imageId: number) => {
    setSelectedImageId(null);
    setTransformers(transformers.filter((id) => id !== imageId));
  };
  const handleStageClick = () => {
    setSelectedImageId(null);
  };

  return (
    <div className="h-[92vh] flex flex-col items-center justify-center">
      <div onClick={roomCode}>초대 코드</div>
      <div onClick={sendData}>클릭하세요</div>
      {socketData ? <img src={socketData.frameUrl} alt="" /> : <></>}
      <Stage width={300} height={350} ref={stageRef}>
        <Layer>
          {originImg && (
            <Image
              image={originImg}
              width={300}
              height={350}
              draggable={false}
              onTap={handleStageClick}
            />
          )}
          {images.map((image) => (
            <Group key={image.id}>
              <Image
                image={image.src}
                x={image.x}
                y={image.y}
                width={image.width}
                height={image.height}
                draggable
                onTap={() => handleSelectImage(image.id)}
              />
              {selectedImageId === image.id && (
                <Transformer
                  nodeRef={stageRef}
                  selectedNode={image.id === selectedImageId}
                  ref={transformerRef}
                  rotateEnabled={true}
                />
              )}
            </Group>
          ))}
        </Layer>
      </Stage>
      <div className="flex justify-center">
        {selectedImageId}
        {assets.map((imageName, index) => (
          <div key={index} className="py-[5vh] px-[5vw] w-[20vw]">
            <img
              className=""
              src={`/frame/${imageName}.png`}
              width={120}
              height={140}
              onClick={() => handleImageClick(imageName)}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default FrameImg;
