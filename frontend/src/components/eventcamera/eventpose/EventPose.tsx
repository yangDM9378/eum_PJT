"use client";

import { useAppSelector } from "@/redux/hooks";
import Image from "next/image";
import React, { useEffect, useState } from "react";
import { pictureEventApi, postPose } from "@/services/eventApi";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/navigation";

const EventPose = () => {
  // 핀 이미지 들어가는곳
  const [pinImg, setPinImg] = useState("");
  const [picImg, setPicImg] = useState("");

  // 핀이미지
  const eventImg = useAppSelector((state) => state.eventReducer.eventimageurl);
  // 사진찍은 이미지
  const picturImg = useAppSelector((state) => state.eventReducer.pictureimg);

  // 그룹 아이디와 핀아이디
  const groupId = useAppSelector((state) => state.coordsReducer.groupId);
  const pinId = useAppSelector((state) => state.coordsReducer.pinId);

  const FormData = require("form-data");
  const formData = new FormData();
  // ulr을 blob으로 바꾸기
  const convertURLtoFile = async (image1: string, image2: string) => {
    const response1 = await fetch(
      `${process.env.NEXT_PUBLIC_IMAGE_URL}${image1}`
    );
    // url -> blob으로 바꾸기
    const data1 = await response1.blob();
    // base64 -> blob으로 바꾸기
    const data2 = await (await fetch(image2)).blob();
    // formdata에 넣어주기
    formData.append("image1", data1, "image1.png");
    formData.append("image2", data2, "image2.png");
  };

  useEffect(() => {
    setResult(-1);
    // url -> blob
    setPinImg(eventImg);
    // base64파일 -> blob
    setPicImg(picturImg);
  }, []);

  // 포즈 결과 상태
  const [result, setResult] = useState<boolean | number>(-1);
  const [loading, setLoading] = useState(false);

  // 포즈 사진 비교하는 함수
  const checkpose = async () => {
    setLoading(true);
    await convertURLtoFile(eventImg, picturImg);
    const response = await postPose(formData);
    setResult(response.result);
    setLoading(false);
  };

  // 포즈가 맞으면
  // 사진 저장 API 통신
  const router = useRouter();
  const queryClient = useQueryClient();
  const poseEventMutation = useMutation(pictureEventApi, {
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["initial-pinpicture"] });
    },
  });

  const savepicture = async () => {
    const imgBlob = await (await fetch(picImg)).blob();

    const jsonReq = { groupId: groupId, pinId: pinId };

    const formData2 = new FormData();
    formData2.append("image", imgBlob, "image.png");
    formData2.append(
      "pictureAddReq",
      new Blob([JSON.stringify(jsonReq)], { type: "application/json" })
    );
    await poseEventMutation.mutate(formData2);
    router.push(`/group/${groupId}`);
  };

  // 다시찍기
  const returnPicture = async () => {
    setResult(-1);
    await router.replace("/eventcamera");
  };

  return (
    <div className="w-[100vw] h-[92vh]">
      <div className="min-h-[6vh] flex justify-center items-center">
        <p className="text-xl text-center font-gmarket-thin">
          같은 포즈를 취했을까요?
        </p>
      </div>
      <div className="min-h-[76vh] px-[2vh] flex flex-col items-center justify-evenly">
        {/* 핀 이미지 */}
        {pinImg && (
          <Image
            src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${pinImg}`}
            alt="pinImg"
            width={400}
            height={500}
            className="rounded-md"
          />
        )}

        {/* 사진찍은 이미지 */}
        {picImg && (
          <Image
            src={picImg}
            alt="picImg"
            width={400}
            height={500}
            className="rounded-md"
          />
        )}
      </div>
      <div className="min-h-[10vh] flex justify-center items-center">
        {result === -1 && (
          <button
            className="py-[1.5vh] w-[45vw] bg-brand-red rounded-md text-white font-gmarket-thin"
            onClick={checkpose}
            disabled={loading}
          >
            {loading ? "로딩 중..." : "확인하기"}
          </button>
        )}
        {result === 1 && (
          <button
            className="py-[1.5vh] w-[45vw] bg-brand-red rounded-md text-white font-gmarket-thin"
            onClick={savepicture}
          >
            저장하기
          </button>
        )}
        {result === 0 && (
          <button
            className="py-[1.5vh] w-[45vw] bg-brand-red rounded-md text-white font-gmarket-thin"
            onClick={returnPicture}
          >
            다시찍기
          </button>
        )}
      </div>
    </div>
  );
};

export default EventPose;
