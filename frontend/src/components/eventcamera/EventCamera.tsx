"use client";

import { useRouter } from "next/navigation";
import { useState, useRef, useEffect } from "react";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { originimageurl } from "@/redux/addevent/addEventSlice";
import { captureImage, startCamera, stopCamera } from "@/utils/getCamera";
import { AiOutlineCamera } from "react-icons/ai";

const EventCamera = () => {
  const dispatch = useAppDispatch();
  const router = useRouter();
  // const pathSelector = useAppSelector((state) => state.coordsReducer.path);
  const [isCameraReady, setIsCameraReady] = useState(false);
  const videoRef = useRef<HTMLVideoElement>(null);

  useEffect(() => {
    // 자동으로 켜져있는 camera 시작
    startCamera(videoRef, setIsCameraReady);
    localStorage.setItem("pathOption", "pose");
  }, []);

  //사진 찰영 버튼 클릭 시
  const handleTakePicture = async () => {
    // 현재 비디오에서 width, height을 지정하여 파일 저장
    const dataURL = await captureImage(videoRef);
    // 카메라 끄기
    stopCamera(videoRef);
    // 이미지 redux를 통해 aging or pose로 이동시키기
    dispatch(originimageurl(dataURL));
    router.push(`/eventcamera/${localStorage.getItem("pathOption")}`);
  };

  return (
    <div className="w-full h-full">
      <div className="h-[88%] flex items-center justify-center">
        <video
          className="rounded-3xl px-[2%]"
          ref={videoRef}
          style={{
            display: isCameraReady ? "block" : "none",
          }}
        />
      </div>
      <div className="flex items-center justify-center h-[12%]">
        <AiOutlineCamera
          className="bg-white rounded-full text-brand-green text-[50px] p-[2%]"
          onClick={handleTakePicture}
        />
      </div>
    </div>
  );
};

export default EventCamera;
