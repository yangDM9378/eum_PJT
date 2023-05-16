"use client";

import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import Konva from "konva";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import { setFrameImg } from "@/redux/map/mapSlice";

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

  // 꾸미기 페이지로 사진 이동시키기
  const router = useRouter();
  const dispatch = useAppDispatch();
  const pictureArt = async () => {
    if (stageRef.current) {
      const tr = stageRef.current.children[0].findOne("Transformer");
      tr.visible(false);
      stageRef.current.draw();
      const dataURL = await stageRef.current.toDataURL({ pixelRatio: 1 });
      dispatch(setFrameImg(dataURL));
      await router.replace(`artpicture/frameimg`);
    }
  };

  return (
    <div className="h-[92vh] flex flex-col items-center justify-center">
      <div className="text-lg font-gmarket-thin mb-[3vh]">
        사진을 꾸밀 프레임을 선택해주세요
      </div>
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
      <button
        className="my-[1vh] bg-brand-blue text-white py-[1.5vh] px-[6vw] rounded-md shadow-xl font-brand-gmarketsans"
        onClick={pictureArt}
      >
        사진 꾸미기
      </button>
    </div>
  );
};

export default framePicture;
