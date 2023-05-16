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

  useEffect(() => {
    if (frameImg) {
      const img = new window.Image();
      img.src = frameImg;
      img.onload = () => {
        setOriginImg(img);
      };
    }
  }, [frameImg]);

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
