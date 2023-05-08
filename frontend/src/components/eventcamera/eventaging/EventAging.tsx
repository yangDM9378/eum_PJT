"use client";
import { useAppSelector } from "@/redux/hooks";
import Konva from "konva";
import { useEffect, useRef, useState } from "react";
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

  const stageRef = useRef<Konva.Stage>(null);

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

  // 사진 저장
  const handleSave = () => {
    const stage = stageRef.current;
    // if (stage) {
    //   // 캔버스 이미지를 data URL로 변환합니다.
    //   const dataURL = stage.toDataURL();

    //   // data URL을 Blob으로 변환합니다.
    //   const blob = dataURLtoBlob(dataURL);

    //   // Blob을 FormData에 추가합니다.
    //   const formData = new FormData();
    //   formData.append("file", blob, "image.png");

    //   // fetch() 메소드를 사용하여 서버로 전송합니다.
    //   fetch("http://example.com/upload", {
    //     method: "POST",
    //     body: formData,
    //   })
    //     .then((response) => {
    //       console.log("Image uploaded successfully");
    //     })
    //     .catch((error) => {
    //       console.error("Failed to upload image", error);
    //     });
    // }
  };

  return (
    <>
      <Stage width={300} height={300} ref={stageRef}>
        <Layer>
          {/* 배경 이미지 */}
          {backgroundImage && (
            <Image image={backgroundImage} width={300} height={300} />
          )}

          {/* 꾸밀 이미지 */}
          <Group draggable>
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
          </Group>
        </Layer>
      </Stage>
      <button onClick={handleSave}>Save</button>
    </>
  );
};

export default EventAging;
