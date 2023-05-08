"use client";
import { useAppSelector } from "@/redux/hooks";
import { agingEventApi } from "@/services/eventApi";
import { AgingEventResult } from "@/types/event";
import { useMutation } from "@tanstack/react-query";
import Konva from "konva";
import { useEffect, useRef, useState } from "react";
import { Stage, Layer, Image, Group, Transformer } from "react-konva";

const EventAging = () => {
  const pictureImg = useAppSelector((state) => state.eventReducer.pictureimg);
  const agingImg = useAppSelector((state) => state.eventReducer.eventimageurl);
  const groupId = useAppSelector((state) => state.coordsReducer.groupId);
  const pinId = useAppSelector((state) => state.coordsReducer.pinId);
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

  // aging결과
  const [response, setResponse] = useState<AgingEventResult | null>(null);

  const stageRef = useRef<Konva.Stage>(null);

  // useEffect(() => {
  //   if (isLoading) {
  //     const img = new window.Image();
  //     img.crossOrigin = "anonymous";
  //     img.src = `${process.env.NEXT_PUBLIC_IMAGE_URL}${agingImg}`;
  //     img.onload = () => {
  //       setIsLoading(false);
  //       setDecorativeImage(img);
  //     };
  //   }
  // }, [agingImg, isLoading]);

  useEffect(() => {
    const img = new window.Image();
    img.src = pictureImg;
    img.onload = () => {
      setBackgroundImage(img);
    };
  }, [pictureImg]);

  // 사진 저장 API 통신
  const agingEventMutation = useMutation(agingEventApi, {
    onSuccess: (data) => {
      setResponse(data);
      // queryClient로 바로 적용되게 사진들
      // queryClient.invalidateQueries({ queryKey: [""] });
    },
  });

  // 사진 저장
  const handleSave = async () => {
    const stage = stageRef.current;
    if (stage) {
      // 캔버스 이미지를 data URL로 변환합니다.
      const dataURL = stage.toDataURL();
      const jsonReq = { groupId: groupId, pinId: pinId };
      const blobRes = await (await fetch(dataURL)).blob();
      const formData = new FormData();
      formData.append("image", blobRes, "image.png");
      formData.append(
        "pictureAddReq",
        new Blob([JSON.stringify(jsonReq)], { type: "application/json" })
      );
      agingEventMutation.mutate(formData);
    }
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
          {/* <Group draggable>
            {decorativeImage && (
              <Image
                image={decorativeImage}
                {...decorativeImageProps}
                draggable={false}
              />
            )}
          </Group> */}
        </Layer>
      </Stage>
      <button onClick={handleSave}>Save</button>
    </>
  );
};

export default EventAging;
