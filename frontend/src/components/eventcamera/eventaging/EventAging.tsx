"use client";
import { useAppSelector } from "@/redux/hooks";
import { pictureEventApi } from "@/services/eventApi";
import { AgingEventResult } from "@/types/event";
import {
  QueryClient,
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";
import Konva from "konva";
import { useRouter } from "next/navigation";
import React, { useEffect, createRef, useRef, useState } from "react";

const EventAging = (): JSX.Element => {
  const pictureImg = useAppSelector((state) => state.eventReducer.pictureimg);
  const agingImg = useAppSelector((state) => state.eventReducer.eventimageurl);
  const groupId = useAppSelector((state) => state.coordsReducer.groupId);
  const pinId = useAppSelector((state) => state.coordsReducer.pinId);
  const stageRef = useRef<Konva.Stage>();

  useEffect(() => {
    const stage = new Konva.Stage({
      container: "container",
      width: window.innerWidth - 20,
      height: 300,
      ref: stageRef,
    });
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
        width: window.innerWidth - 10,
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

  const handleLayer = async () => {};

  // 사진 저장 API 통신
  const queryClient = useQueryClient();
  const agingEventMutation = useMutation(pictureEventApi, {
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["initial-pinpicture"] });
    },
  });

  // 사진 저장
  // aging결과
  const router = useRouter();
  const handleSave = async () => {
    if (stageRef.current) {
      const tr = stageRef.current.children[0].findOne("Transformer");
      tr.visible(false);
      stageRef.current.draw();
      const dataURL = await stageRef.current.toDataURL({ pixelRatio: 1 });

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
      await router.replace(`/map/${groupId}`);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center w-full h-[92vh]">
      <div id="container" className="border rounded-lg h-[500]"></div>
      <button
        className="my-[2vh] bg-brand-blue text-white py-[1.5vh] px-[6vw] rounded-md shadow-xl font-brand-gmarketsans"
        onClick={handleSave}
      >
        사진꾸미기완료
      </button>
    </div>
  );
};

export default EventAging;
