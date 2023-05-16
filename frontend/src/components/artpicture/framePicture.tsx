"use client";

import { useAppSelector } from "@/redux/hooks";
import Konva from "konva";
import Image from "next/image";
import { useEffect, useRef, useState } from "react";

const framePicture = (): JSX.Element => {
  const pictureImg = useAppSelector((state) => state.coordsReducer.frameImg);
  const stageRef = useRef<Konva.Stage>();
  const layerRef = useRef<Konva.Layer>();
  const [selectFrame, setSelectFrame] = useState("");

  useEffect(() => {
    const stage = new Konva.Stage({
      container: "container",
      width: 300,
      height: 350,
      ref: stageRef,
    });
    const layer = new Konva.Layer({ ref: layerRef });
    stage.add(layer);

    if (pictureImg) {
      const bgimage = new window.Image();
      bgimage.crossOrigin = "Anonymous";
      bgimage.src = `${process.env.NEXT_PUBLIC_IMAGE_URL}${pictureImg}`;
      bgimage.onload = () => {
        const bgrect = new Konva.Image({
          x: 0,
          y: 0,
          image: bgimage,
          width: 300,
          height: 350,
          name: "bgrect",
          draggable: true,
          listening: true,
        });
        layer.add(bgrect);
        // 사진크기 조절 및 회전
        const tr = new Konva.Transformer({
          visible: true,
        });
        layer.add(tr);
        tr.nodes([bgrect]);
        layer.on("tap", function () {
          tr.visible(!tr.visible());
          stage.draw();
        });
        stageRef.current?.draw();
      };
    }

    if (selectFrame) {
      const frameimage = new window.Image();
      frameimage.crossOrigin = "Anonymous";
      frameimage.src = `${selectFrame}`;
      frameimage.onload = () => {
        const framerect = new Konva.Image({
          x: 0,
          y: 0,
          image: frameimage,
          width: 300,
          height: 350,
          name: "framerect",
          draggable: false,
          listening: false,
        });
        layer.add(framerect);
        stageRef.current?.draw();
      };
    }

    stageRef.current = stage;
  }, [pictureImg, selectFrame]);

  const images = [1, 2, 3, 4];

  return (
    <div className="h-[90vh] flex flex-col items-center justify-center">
      <div
        id="container"
        className="flex items-center justify-center border rounded-lg"
      ></div>
      <div className="flex justify-center">
        {images.map((imageName, index) => (
          <div key={index} className="py-[5vh] px-[5vw] w-[20vw]">
            <Image
              className=""
              src={`/frame/${imageName}.png`}
              alt={`Image ${index + 1}`}
              width={120}
              height={140}
              onClick={() => {
                setSelectFrame(`/frame/${imageName}.png`);
              }}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default framePicture;
