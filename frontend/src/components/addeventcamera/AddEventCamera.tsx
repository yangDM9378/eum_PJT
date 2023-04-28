"use client";

import { useRouter } from "next/navigation";
import { useState, useRef, useEffect } from "react";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { originimageurl } from "@/redux/addevent/addEventSlice";

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
    // 페이지 랜더시 자동으로 camera 켜지게 하기
    const startCamera = async () => {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({
          video: true,
        });
        if (videoRef.current) {
          videoRef.current.srcObject = stream;
          videoRef.current.play();
          setIsCameraReady(true);
        }
      } catch (err) {
        console.error(err);
      }
    };

    startCamera();
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
      // 이미지 localStorage를 통해 removebg로 이동시키기
      const dataURL = canvas.toDataURL("image/png");
      await dispatch(originimageurl(dataURL));
      await router.push(`/addeventcamera/${pathOption}`);
    }
  };

  return (
    <>
      <div>
        <video
          ref={videoRef}
          style={{ display: isCameraReady ? "block" : "none" }}
        ></video>
      </div>
      <div>
        <button onClick={handleTakePicture}>사진 촬영</button>
      </div>
    </>
  );
};

export default AddEvnetCamera;
