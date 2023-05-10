"use client";
import { useAppSelector } from "@/redux/hooks";
import { agingEventApi } from "@/services/eventApi";
import { AgingEventResult } from "@/types/event";
import { useMutation } from "@tanstack/react-query";
import Konva from "konva";
import React, { useEffect, createRef, useRef, useState } from "react";

const EventAging = (): JSX.Element => {
  const pictureImg = useAppSelector((state) => state.eventReducer.pictureimg);
  const agingImg = useAppSelector((state) => state.eventReducer.eventimageurl);
  const groupId = useAppSelector((state) => state.coordsReducer.groupId);
  const pinId = useAppSelector((state) => state.coordsReducer.pinId);
  const stageRef = useRef<Konva.Stage>();

  const makeInitStage = () => {
    const stage = new Konva.Stage({
      container: "container",
      width: window.innerWidth,
      height: 300,
      ref: stageRef,
    });
    return stage;
  };

  useEffect(() => {
    const stage = makeInitStage();
    const layer = new Konva.Layer();
    stage.add(layer);
    const bgimage = new window.Image();
    bgimage.crossOrigin = "Anonymous";
    bgimage.src = pictureImg;
    bgimage.onload = () => {
      const bgrect = new Konva.Image({
        x: 0,
        y: 0,
        image: bgimage,
        width: window.innerWidth,
        height: 300,
        name: "bgrect",
        draggable: false,
        listening: false,
      });
      layer.add(bgrect);
    };

    const decoimage = new window.Image();
    decoimage.crossOrigin = "Anonymous";
    decoimage.src = `${process.env.NEXT_PUBLIC_IMAGE_URL}${agingImg}`;
    decoimage.onload = () => {
      const decorect = new Konva.Image({
        x: 0,
        y: 0,
        width: 150,
        height: 150,
        image: decoimage,
        name: "decorect",
        draggable: true,
        listening: true,
        transformsEnabled: "all",
      });
      layer.add(decorect);
      // 사진크기 조절 및 회전
      const tr = new Konva.Transformer({
        visible: true,
      });
      layer.add(tr);
      tr.nodes([decorect]);
      layer.on("tap", function () {
        tr.visible(!tr.visible());
        stage.draw();
      });
      stageRef.current = stage;
    };
  }, []);

  // 사진 저장 API 통신
  const agingEventMutation = useMutation(agingEventApi, {
    onSuccess: (data) => {
      console.log(data);
      setResponse(data);
    },
  });

  // 사진 저장
  // aging결과
  const [response, setResponse] = useState<AgingEventResult | null>(null);

  const handleSave = async () => {
    if (stageRef.current) {
      const dataURL = await stageRef.current.toDataURL({ pixelRatio: 1 });
      console.log(dataURL);

      // 캔버스 이미지를 data URL로 변환합니다.
      const jsonReq = { groupId: groupId, pinId: pinId };
      const blobRes = await (await fetch(dataURL)).blob();

      const formData = new FormData();
      formData.append("image", blobRes, "image.png");
      formData.append(
        "pictureAddReq",
        new Blob([JSON.stringify(jsonReq)], { type: "application/json" })
      );
      await agingEventMutation.mutate(formData);
    }
  };

  return (
    <>
      <div id="container"></div>
      <button onClick={handleSave}>사진꾸미기완료</button>
    </>
  );
};

export default EventAging;
