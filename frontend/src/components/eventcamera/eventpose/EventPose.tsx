"use client";

import { eventimageurl } from "@/redux/doevent/eventSlice";
import { useAppSelector } from "@/redux/hooks";
import Image from "next/image";
import React, { useEffect, useState } from "react";
import { pictureEventApi, postPose } from "@/services/eventApi";
import { useMutation } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { isNull } from "util";

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

  const formData = new FormData();
  // ulr을 blob으로 바꾸기
  const convertURLtoFile = async (image1: string, image2: string) => {
    console.log(`${process.env.NEXT_PUBLIC_IMAGE_URL}${image1}`);

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
    // url -> blob
    setPinImg(eventImg);
    // base64파일 -> blob
    setPicImg(picturImg);
  }, []);

  // 포즈 결과 상태
  const [result, setResult] = useState<boolean | number>(-1);

  // 포즈 사진 비교하는 함수
  const checkpose = async () => {
    await convertURLtoFile(eventImg, picturImg);
    console.log(formData);
    const response = await postPose(formData);
    setResult(response.result);
  };

  // 포즈가 맞으면
  // 사진 저장 API 통신
  const router = useRouter();
  const poseEventMutation = useMutation(pictureEventApi, {
    onSuccess: (data) => {},
  });

  const savepicture = async () => {
    const imgBlob = await (await fetch(picImg)).blob();

    const jsonReq = { groupId: groupId, pinId: pinId };

    const formData1 = new FormData();
    formData1.append("image", imgBlob, "image.png");
    formData1.append(
      "pictureAddReq",
      new Blob([JSON.stringify(jsonReq)], { type: "application/json" })
    );
    await poseEventMutation.mutate(formData1);
    await router.push(`/group/${groupId}`);
  };

  // 다시찍기
  const returnPicture = async () => {
    setResult(-1);
    await router.push("/eventcamera");
  };

  return (
    <>
      <p className=" font-gmarket-thin text-center text-lg">
        두 사진이 포즈가 같나요?
      </p>
      <div className="flex-col justify-center h-[60%] pt-[3%]">
        {/* 핀 이미지 */}
        <Image
          src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${pinImg}`}
          alt="pinImg"
          width={400}
          height={500}
          className="pt-[5%] h-[35vh] px-3"
        />
        {/* 사진찍은 이미지 */}
        <Image
          src={picImg}
          alt="picImg"
          width={400}
          height={700}
          className="pt-[5%] h-[35vh] px-3"
        />
      </div>
      <div className="flex justify-center  pt-[5%] ">
        <button
          className="w-[50%] h-[2.5rem] absolute bottom-[10%] bg-brand-red rounded-md text-white font-gmarket-thin "
          onClick={checkpose}
        >
          확인하기
        </button>
        {result ? (
          <button
            className="w-[50%] h-[2.5rem] absolute bottom-[10%] bg-brand-red rounded-md text-white font-gmarket-thin "
            onClick={savepicture}
          >
            저장하기
          </button>
        ) : (
          <button
            className="w-[50%] h-[2.5rem] absolute bottom-[10%] bg-brand-red rounded-md text-white font-gmarket-thin "
            onClick={returnPicture}
          >
            다시찍기
          </button>
        )}
      </div>
    </>
  );
};

export default EventPose;
