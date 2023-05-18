"use client";

import { useAppSelector } from "@/redux/hooks";
import { useEffect, useState, useRef } from "react";
import { Stage, Layer, Image, Group, Transformer } from "react-konva";

const FrameImg = () => {
  const [originImg, setOriginImg] = useState<CanvasImageSource | undefined>(
    undefined
  );
  const frameImg = useAppSelector((state) => state.coordsReducer.frameImg);
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

  const [icons, setIcons] = useState<
    {
      id: number;
      src: CanvasImageSource;
      x: number;
      y: number;
      width: number;
      height: number;
    }[]
  >([]);

  const [selectedId, setSelectedId] = useState(0);
  const [nextImageId, setNextImageId] = useState(0); // 초기 이미지 ID

  // 선택한거 취소하게 하는함수
  const checkDeselect = (e: any) => {
    // deselect when clicked on empty area
    const clickedOnEmpty = e.target === e.target.getStage();
    if (clickedOnEmpty) {
      setSelectedId(-1);
    }
  };

  useEffect(() => {}, [selectedId]);

  // 아이콘 업데이트 하는 함수
  const handleChange = (title: number) => {
    const newIcon = new window.Image();
    newIcon.src = `/icons/${title}.png`;
    newIcon.onload = () => {
      const newKonvaImage = new window.Image();
      newKonvaImage.src = newIcon.src;
      newKonvaImage.onload = () => {
        const iconId = nextImageId; // 새로운 이미지 ID 할당
        // setNextImageId((prevId) => prevId + 1); // 다음 이미지 ID 업데이트
        setNextImageId(nextImageId + 1); // 다음 이미지 ID 업데이트

        const updatedIcons = [
          ...icons,
          {
            id: iconId,
            src: newIcon,
            x: 0,
            y: 0,
            width: 120,
            height: 120,
          },
        ];
        setIcons(updatedIcons);
      };
    };
  };

  return (
    <div className="h-[92vh] flex flex-col items-center justify-center">
      {/* 캔버스 */}
      <Stage width={300} height={350} ref={stageRef}>
        <Layer>
          {originImg && (
            <Image
              image={originImg}
              alt="frameImg"
              width={300}
              height={350}
              draggable={false}
              // onMouseDown={checkDeselect}
              // onTouchStart={checkDeselect}
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
