"use client";
import { useAppSelector } from "@/redux/hooks";
import { useEffect, useState } from "react";
import { Stage, Layer, Image, Group } from "react-konva";

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
  const [decorativeImage, setDecorativeImage] =
    useState<HTMLImageElement | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [backgroundImage, setBackgroundImage] = useState<
    HTMLImageElement | undefined
  >(undefined);

  useEffect(() => {
    if (isLoading) {
      const img = new window.Image();
      img.src = `${process.env.NEXT_PUBLIC_IMAGE_URL}group/image/${agingImg}`;
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
          {decorativeImage && (
            <Image
              image={decorativeImage}
              {...decorativeImageProps}
              draggable={false}
            />
          )}
        </Group>
      </Layer>
    </Stage>
  );
};

export default EventAging;
