"use client";

import { useRouter } from "next/navigation";
import { useState, useRef, useEffect } from "react";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { pictureimg } from "@/redux/doevent/eventSlice";
import { captureImage, startCamera, stopCamera } from "@/utils/getCamera";

import { AiOutlineCamera } from "react-icons/ai";

import { AiOutlineInfoCircle } from "react-icons/ai";
import InfoModal from "../modals/InfoModal";

const EventCamera = () => {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const [isCameraReady, setIsCameraReady] = useState(false);
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
    startCamera(videoRef, setIsCameraReady);
  }, []);

  //사진 찰영 버튼 클릭 시
  const handleTakePicture = async () => {
    // 현재 비디오에서 width, height을 지정하여 파일 저장
    const dataURL = await captureImage(videoRef);
    // 카메라 끄기
    stopCamera(videoRef);
    dispatch(pictureimg(dataURL)); // base 64파일
    await router.push(`/eventcamera/${eventType}`);
  };

  return (
    <div className="w-full h-full">
      <div className="h-[88%] mt-[30%] flex items-center justify-center">
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
          className="bg-white rounded-full text-brand-green text-[50px] my-[8%] p-[2%]
          "
          onClick={handleTakePicture}
        />
      </div>

      {eventType == "pose" && (
        <div>
          <AiOutlineInfoCircle
            onClick={OpenInfoModal}
            className="h-[10%] w-[10%]"
          />
        </div>
      )}
      <InfoModal isOpen={modalOpen} setIsOpen={setModalOpen} />
    </div>
  );
};

export default EventCamera;
