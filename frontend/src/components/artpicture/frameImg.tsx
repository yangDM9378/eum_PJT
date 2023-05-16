"use client";

import { useAppSelector } from "@/redux/hooks";

import { useEffect, useState } from "react";
import { Stage, Layer, Group, Image } from "react-konva";
import FrameImgChild from "./frameImgChild";

const frameImg = () => {
  const [originImg, setOriginImg] = useState<HTMLImageElement | undefined>(
    undefined
  );
  const frameImg = useAppSelector((state) => state.coordsReducer.frameImg);

  useEffect(() => {
    if (frameImg) {
      const img = new window.Image();
      img.src = frameImg;
      img.onload = () => {
        setOriginImg(img);
      };
    }
  }, []);
  const initialicons = [
    {
      x: 10,
      y: 10,
      width: 50,
      height: 50,
      title: 1,
    },
    {
      x: 10,
      y: 10,
      width: 50,
      height: 50,
      title: 2,
    },
    {
      x: 10,
      y: 10,
      width: 50,
      height: 50,
      title: 3,
    },
    {
      x: 10,
      y: 10,
      width: 50,
      height: 50,
      title: 4,
    },
    {
      x: 10,
      y: 10,
      width: 50,
      height: 50,
      title: 5,
    },
    {
      x: 10,
      y: 10,
      width: 50,
      height: 50,
      title: 6,
    },
    {
      x: 10,
      y: 10,
      width: 50,
      height: 50,
      title: 7,
    },
  ];

  const [icons, setIcons] = useState(initialicons);
  const [selectedId, setSelectedId] = useState(0);

  useEffect(() => {
    console.log(selectedId, "🎈");
  }, [selectedId]);

  // 선택한거 취소하게 하는함수
  const checkDeselect = (e: any) => {
    const clickedOnEmpty = e.target === e.target.getStage();
    if (clickedOnEmpty) {
      setSelectedId(0);
    }
    e.cancelBubble = true;
  };

  return (
    <div className="h-[92vh] flex flex-col items-center justify-center">
      <Stage
        width={360}
        height={350}
        onMouseDown={checkDeselect}
        onTouchStart={checkDeselect}
      >
        <Layer>
          {originImg && (
            <div className="flex items-center justify-center border rounded-lg">
              <Image
                image={originImg}
                alt="frameImg"
                width={300}
                height={350}
              />
            </div>
          )}

          {icons.map((icon, i) => (
            <FrameImgChild
              key={i}
              shapeProps={icon}
              onSelect={() => {
                setSelectedId(i);
              }}
              isSelected={i === selectedId}
              onChange={(newAttrs) => {
                const newicons = icons.slice();
                newicons[i] = newAttrs;
                setIcons(newicons);
              }}
            />
          ))}
        </Layer>
      </Stage>



      <div className="flex mt-[5%]">
        {initialicons.map((iconName, idx) => {
          const newimg = new window.Image();
          newimg.src = `/icons/${iconName.title}.png`;
          return (
            <Group key={idx}>
              <Image
                className=""
                image={newimg}
                alt=""
                width={50}
                height={100}
                style={{ width: iconName.width, height: iconName.height }}
              />
            </Group>
          );
        })}
      </div>
    </div>
  );
};

export default frameImg;
