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
    try {
      setLoading(true);
      await convertURLtoFile(eventImg, picturImg);
      const response = await postPose(formData);
      console.log(response.result);
      if (response.result == false) {
        throw new Error("포즈를 다시 취해주세요");
      }
      setResult(response.result);
    } catch (error) {
      alert("사진이 잘못 됐어요 다시 사진을 찍어주세요");
    } finally {
      setLoading(false);
    }
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
    router.push(`/map/${groupId}`);
  };

  // 다시찍기
  const returnPicture = async () => {
    setResult(-1);
    await router.replace("/eventcamera");
  };

  return (
    <div className="w-[100vw] h-[92vh]">
      <p className="font-gmarket-thin text-center py-[1vh] text-[3vh]">
        같은 포즈를 취했을까요?
      </p>

      {/* 핀 이미지 */}
      <div className="h-[76vh] flex flex-col justify-evenly items-center">
        {pinImg && (
          <Image
            className="rounded-lg jborder border-brand-blue border-spacing-1 drop-hadow-2xl"
            src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${pinImg}`}
            alt="pinImg"
            width={320}
            height={260}
          />
        )}

        {/* 사진찍은 이미지 */}
        {picImg && (
          <Image
            className="border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
            src={picImg}
            alt="picImg"
            width={320}
            height={260}
          />
        )}
      </div>
      <div className="h-[8vh] flex items-center justify-center">
        {result === -1 && (
          <button
            className="py-[1.5vh] my-[2vh] w-[45vw] bg-brand-red rounded-md text-white font-gmarket-thin"
            onClick={checkpose}
            disabled={loading}
          >
            {loading ? "로딩 중..." : "확인하기"}
          </button>
        )}
        {result === 1 && (
          <button
            className="py-[1.5vh] my-[2vh] w-[45vw] bg-brand-red rounded-md text-white font-gmarket-thin"
            onClick={savepicture}
          >
            포즈 저장하기
          </button>
        )}
        {result === 0 && (
          <button
            className="py-[1.5vh] my-[2vh] w-[45vw] bg-brand-red rounded-md text-white font-gmarket-thin"
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
