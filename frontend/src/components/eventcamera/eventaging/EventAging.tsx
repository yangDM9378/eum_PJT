"use client";
import { useAppSelector } from "@/redux/hooks";
import { useState } from "react";
import { Stage, Layer, Image, Transformer, Group } from "react-konva";

const EventAging = () => {
  const pictureImg = useAppSelector((state) => state.eventReducer.pictureimg);
  const agingImg = useAppSelector((state) => state.eventReducer.eventimageurl);
  const [decorativeImageProps, setDecorativeImageProps] = useState({
    x: 10,
    y: 0,
    width: 300,
    height: 300,
  });
  const [selected, setSelected] = useState(false);

  const backgroundImage = new window.Image();
  backgroundImage.src = pictureImg;

  const decorativeImage = new window.Image();
  decorativeImage.src = `${process.env.NEXT_PUBLIC_IMAGE_URL}${agingImg}`;

  return (
    <Stage width={300} height={300}>
      <Layer>
        {/* 배경 이미지 */}
        <Image image={backgroundImage} width={300} height={300} />

        {/* 꾸밀 이미지 */}
        <Group
          draggable
          onClick={() => {
            setSelected(true);
          }}
          onDragEnd={() => {
            setSelected(false);
          }}
        >
          <Image
            image={decorativeImage}
            {...decorativeImageProps}
            draggable={false}
          />
          {/* <Transformer
            keepRatio={true}
            anchorSize={10}
            borderEnabled={true} // 변경된 부분
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
          /> */}
        </Group>
      </Layer>
    </Stage>
  );
};

export default EventAging;
