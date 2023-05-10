"use client";

import { eventimageurl } from "@/redux/doevent/eventSlice";
import { useAppSelector } from "@/redux/hooks";
import Image from "next/image";
import React, { useEffect, useState } from "react";
import { postPose } from "@/services/eventApi";

const EventPose = () => {
  // 핀 이미지 들어가는곳
  const [pinImg, setPinImg] = useState("");
  const [picImg, setPicImg] = useState("");

  // 핀이미지
  const eventImg = useAppSelector((state) => state.eventReducer.eventimageurl);
  // 사진찍은 이미지
  const picturImg = useAppSelector((state) => state.eventReducer.pictureimg);

  const formData = new FormData();
  // ulr을 blob으로 바꾸기
  const convertURLtoFile = async (image1: string, image2: string) => {
    console.log(`${process.env.NEXT_PUBLIC_IMAGE_URL}${image1}`);
    const response1 = await fetch(
      `${process.env.NEXT_PUBLIC_IMAGE_URL}${image1}`
    );
    // url -> blob으로 바꾸기
    const data1 = await response1.blob();
    console.log(data1,'👻')
    // base64 -> blob으로 바꾸기
    const data2 = await (await fetch(image2)).blob();
    console.log(data2,'❤');

    // formdata에 넣어주기
    formData.append("image1", data1, "image1.png");
    formData.append("image2", data2, "image2.png");
  };

  useEffect(() => {
    // url -> blob
    setPinImg(eventImg);
    // base64파일 -> blob
    setPicImg(picturImg);
    convertURLtoFile(eventImg, picturImg);
  }, []);

  // 포즈 결과 상태
  const [result, setResult] = useState<boolean>(false);

  // 포즈 사진 비교하는 함수
  const checkpose = async () => {
    const response = await postPose(formData);
    console.log(response);
    // setResult(response.data);
  };

  return (
    <>
      <p className=" font-gmarket-thin text-center text-lg">
        두 사진이 포즈가 같나요?
      </p>
      <div className="flex-col justify-center h-[60%] pt-[3%]">
        {/* 핀 이미지 */}

        <Image
          src={pinImg}
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
      </div>
    </>
  );
};

export default EventPose;
