"use client";

import { useRouter } from "next/navigation";
import { useState, useRef, useEffect } from "react";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { originimageurl } from "@/redux/addevent/addEventSlice";
import { startCamera } from "@/utils/getCamera";

const AddEvnetCamera = () => {
  const [isCameraReady, setIsCameraReady] = useState(false);
  const [pathOption, setPathOption] = useState("");
  const dispatch = useAppDispatch();
  const pathSelector = useAppSelector((state) => state.coordsReducer.path);

  const videoRef = useRef<HTMLVideoElement>(null);
  const router = useRouter();
  useEffect(() => {
    // 여기서 redux에서 pathOption값 가져와서 setPathOption 하기
    setPathOption(pathSelector);
    // 자동으로 켜져있는 camera 시작
    const startcamera = async () => {
      await startCamera(videoRef, setIsCameraReady);
    };
    startcamera();
  }, []);

  //사진 찰영 버튼 클릭 시
  const handleTakePicture = async () => {
    // 현재 비디오에서 width, height을 지정하여 파일 저장
    if (videoRef.current) {
      const canvas = document.createElement("canvas");
      canvas.width = videoRef.current.videoWidth;
      canvas.height = videoRef.current.videoHeight;
      canvas
        .getContext("2d")
        ?.drawImage(videoRef.current, 0, 0, canvas.width, canvas.height);

      // 카메라 끄기
      const stream = videoRef.current.srcObject as MediaStream;
      if (stream) {
        const tracks = stream.getTracks();
        tracks.forEach((track) => track.stop());
      }
      // 이미지 redux를 통해 aging or pose로 이동시키기
      const dataURL = canvas.toDataURL("image/png");
      dispatch(originimageurl(dataURL));
      router.push(`/addeventcamera/${pathOption}`);
    }
  };

  return (
    <div className="w-full h-full bg-black">
      <video
        ref={videoRef}
        style={{ display: isCameraReady ? "block" : "none" }}
      ></video>
      <div>
        <button onClick={handleTakePicture}>사진 촬영</button>
      </div>
    </div>
  );
};

export default AddEvnetCamera;
