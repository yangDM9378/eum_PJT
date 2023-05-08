"use client";
import { useAppSelector } from "@/redux/hooks";
import { useEffect, useState } from "react";
import { Stage, Layer, Image, Group, Transformer } from "react-konva";

const EventAging = () => {
  const pictureImg = useAppSelector((state) => state.eventReducer.pictureimg);
  const agingImg = useAppSelector((state) => state.eventReducer.eventimageurl);
  const [decorativeImageProps, setDecorativeImageProps] = useState({
    x: 10,
    y: 0,
    width: 150,
    height: 150,
    rotation: 0,
  });
  const [selected, setSelected] = useState(false);
  const [decorativeImage, setDecorativeImage] =
    useState<HTMLImageElement | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [backgroundImage, setBackgroundImage] = useState<
    HTMLImageElement | undefined
  >(undefined);

  useEffect(() => {
    if (isLoading) {
      const img = new window.Image();
      img.src = `${process.env.NEXT_PUBLIC_IMAGE_URL}${agingImg}`;
      img.onload = () => {
        setIsLoading(false);
        setDecorativeImage(img);
      };
    }
  }, [agingImg, isLoading]);

  useEffect(() => {
    const img = new window.Image();
    img.src = pictureImg;
    img.onload = () => {
      setBackgroundImage(img);
    };
  }, [pictureImg]);

  return (
    <Stage width={300} height={300}>
      <Layer>
        {/* 배경 이미지 */}
        {backgroundImage && (
          <Image image={backgroundImage} width={300} height={300} />
        )}
      </Layer>
      {/* 꾸밀 이미지 */}
      <Layer draggable>
        {decorativeImage && (
          <Image
            image={decorativeImage}
            {...decorativeImageProps}
            draggable={false}
          />
        )}
        <Transformer
          keepRatio={true}
          anchorSize={10}
          borderEnabled={true}
          rotateEnabled={false}
          ref={(node) => {
            if (node) {
              const layer = node.getLayer();
              if (layer) {
                layer.batchDraw();
              }
            }
          }}
          {...decorativeImageProps}
          onTransform={(event) => {
            const node = event.target;
            const scaleX = node.scaleX();
            const scaleY = node.scaleY();
            const width = Math.max(5, node.width() * scaleX);
            const height = Math.max(5, node.height() * scaleY);

            // rotation 속성을 추가하여 새로운 객체를 생성합니다.
            const newProps = {
              x: node.x(),
              y: node.y(),
              width: width,
              height: height,
              rotation: node.rotation(),
            };

            setDecorativeImageProps(newProps);
          }}
        />
      </Layer>
    </Stage>
  );
};

export default EventAging;
