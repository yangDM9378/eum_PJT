"use client";

import { useRouter, usePathname } from "next/navigation";
import { useState, useRef, useEffect, use } from "react";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { originimageurl } from "@/redux/addevent/addEventSlice";
import { captureImage, startCamera, stopCamera } from "@/utils/getCamera";
import { AiOutlineCamera } from "react-icons/ai";
import { RiCameraSwitchLine } from "react-icons/ri";
import BackIcon from "../common/BackIcon";

const AddEventCamera = () => {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const routerPath = usePathname();
  const pathSelector = useAppSelector((state) => state.coordsReducer.path);
  const [pathOption, setPathOption] = useState(pathSelector);
  const [isCameraReady, setIsCameraReady] = useState(false);
  const [isFrontCamera, setIsFrontCamera] = useState(false);
  const videoRef = useRef<HTMLVideoElement>(null);

  // 자동으로 켜져있는 camera 시작
  useEffect(() => {
    startCamera(videoRef, setIsCameraReady, isFrontCamera);
  }, [isFrontCamera]);

  //사진 찰영 버튼 클릭 시
  const handleTakePicture = async () => {
    // 현재 비디오에서 width, height을 지정하여 파일 저장
    const dataURL = await captureImage(videoRef);
    // 이미지 redux를 통해 aging or pose로 이동시키기
    dispatch(originimageurl(dataURL));
    stopCamera(videoRef);
    await router.push(`/addeventcamera/${pathOption}`);
  };

  // 화면전환
  const chageScreen = async () => {
    setIsFrontCamera(!isFrontCamera);
    await stopCamera(videoRef);
  };

  // 뒤로가기
  const goBack = async () => {
    setIsFrontCamera(false);
    stopCamera(videoRef);
    await router.back();
  };

  return (
    <div className="w-full h-full">
      <div className="h-[8%] text-black">
        <button onClick={goBack}>
          <BackIcon />
        </button>
      </div>
      <div className="flex flex-col items-center justify-center">
        <video
          className="rounded-3xl px-[2%]"
          ref={videoRef}
          style={{
            display: isCameraReady ? "block" : "none",
          }}
        />
        <div className="grid grid-cols-3 items-center place-items-center justify-between w-full px-[2%] py-[5%]">
          <div className="grid cols-span-1"></div>
          <AiOutlineCamera
            className="grid cols-span-1 bg-white rounded-full text-brand-green text-[50px] p-[2%]"
            onClick={handleTakePicture}
          />
          <RiCameraSwitchLine
            className="grid cols-span-1 text-gray-400 text-[40px] p-[2%]"
            onClick={chageScreen}
          />
        </div>
      </div>
    </div>
  );
};

export default AddEventCamera;
