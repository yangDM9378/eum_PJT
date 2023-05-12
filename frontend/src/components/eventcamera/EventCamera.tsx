"use client";

import { useRouter } from "next/navigation";
import { useState, useRef, useEffect } from "react";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { pictureimg } from "@/redux/doevent/eventSlice";
import { captureImage, startCamera, stopCamera } from "@/utils/getCamera";
import { AiOutlineCamera } from "react-icons/ai";
import { AiOutlineInfoCircle } from "react-icons/ai";
import InfoModal from "../modals/InfoModal";
import { RiCameraSwitchLine } from "react-icons/ri";

const EventCamera = () => {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const [isCameraReady, setIsCameraReady] = useState(false);
  const [isFrontCamera, setIsFrontCamera] = useState(false);
  const videoRef = useRef<HTMLVideoElement>(null);
  const eventType = useAppSelector((state) => state.eventReducer.eventtype);
  //모달관련 상태
  const [modalOpen, setModalOpen] = useState<boolean>(false);

  //모달 열기
  const OpenInfoModal = () => {
    setModalOpen(true);
  };

  //등록한 포즈 사진 가져오기

  useEffect(() => {
    // 자동으로 켜져있는 camera 시작
    startCamera(videoRef, setIsCameraReady, isFrontCamera);
  }, [isFrontCamera]);

  //사진 찰영 버튼 클릭 시
  const handleTakePicture = async () => {
    // 현재 비디오에서 width, height을 지정하여 파일 저장
    const dataURL = await captureImage(videoRef);
    // 카메라 끄기
    stopCamera(videoRef);
    dispatch(pictureimg(dataURL)); // base 64파일
    await router.replace(`/eventcamera/${eventType}`);
  };

  // 화면전환
  const chageScreen = async () => {
    setIsFrontCamera(!isFrontCamera);
    await stopCamera(videoRef);
  };

  return (
    <div className="w-full h-full">
      <div className="flex flex-col h-[88%] items-center justify-center">
        <video
          className="rounded-3xl px-[2%]"
          ref={videoRef}
          style={{
            display: isCameraReady ? "block" : "none",
          }}
        />
      </div>

      <div className="grid grid-cols-3 items-center place-items-center justify-between w-full px-[2%] py-[5%]">
        {eventType == "pose" ? (
          <AiOutlineInfoCircle
            onClick={OpenInfoModal}
            className="grid cols-span-1 text-gray-400 text-[40px] p-[2%]"
          />
        ) : (
          <div className="grid cols-span-1"></div>
        )}
        <AiOutlineCamera
          className="grid cols-span-1 bg-white rounded-full text-brand-green text-[50px] my-[8%] p-[2%]"
          onClick={handleTakePicture}
        />

        <RiCameraSwitchLine
          className="grid cols-span-1 text-gray-400 text-[40px] p-[2%]"
          onClick={chageScreen}
        />
        <InfoModal isOpen={modalOpen} setIsOpen={setModalOpen} />
      </div>
    </div>
  );
};

export default EventCamera;
