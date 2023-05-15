"use client";

import { useRouter, usePathname } from "next/navigation";
import { useState, useRef, useEffect } from "react";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { pictureimg } from "@/redux/doevent/eventSlice";
import { captureImage, startCamera, stopCamera } from "@/utils/getCamera";
import { AiOutlineCamera } from "react-icons/ai";
import { AiOutlineInfoCircle } from "react-icons/ai";
import InfoModal from "../modals/InfoModal";
import { RiCameraSwitchLine } from "react-icons/ri";
import BackIcon from "../common/BackIcon";

const EventCamera = () => {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const pathName = usePathname();
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
  // 뒤로가기
  const goBack = async () => {
    setIsFrontCamera(false);
    stopCamera(videoRef);
    await router.back();
  };

  return (
    <div className="w-[100vw] h-[100vh]">
      <div className="h-[8vh] text-black">
        <button onClick={goBack}>
          <BackIcon />
        </button>
      </div>
      {eventType == "pose" ? (
        <div className="font-gmarket-thin text-center my-[1vh] text-[3vh]">
          포즈를 따라해 보세요
        </div>
      ) : (
        <div className="font-gmarket-thin text-center my-[1vh] text-[3vh]">
          사진을 찍어주세요
        </div>
      )}
      <div className="min-h-[77vh] flex flex-col items-center justify-center">
        <video
          className="rounded-3xl px-[2%]"
          ref={videoRef}
          style={{
            display: isCameraReady ? "block" : "none",
          }}
        />
      </div>
      <div className="flex h-[10vh] justify-center items-center">
        <div className="grid w-full grid-cols-3 pt-2 place-items-center ">
          {eventType == "pose" ? (
            <div>
              <AiOutlineInfoCircle
                onClick={OpenInfoModal}
                className="grid cols-span-1 text-brand-red text-[40px] p-[2%]"
              />
            </div>
          ) : (
            <div className="grid cols-span-1"></div>
          )}
          <AiOutlineCamera
            className="grid cols-span-1 bg-white rounded-full text-brand-green text-[50px] my-[8%] p-[2%]"
            onClick={handleTakePicture}
          />

          <RiCameraSwitchLine
            className="grid cols-span-1 text-brand-red text-[40px] p-[2%]"
            onClick={chageScreen}
          />
          <InfoModal isOpen={modalOpen} setIsOpen={setModalOpen} />
        </div>
      </div>
    </div>
  );
};

export default EventCamera;
