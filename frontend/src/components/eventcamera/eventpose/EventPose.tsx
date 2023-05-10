"use client";

import { eventimageurl } from "@/redux/doevent/eventSlice";
import { useAppSelector } from "@/redux/hooks";
import Image from "next/image";
import React, { useEffect, useState } from "react";

const EventPose = () => {
  // 핀 이미지 들어가는곳
  const [pinImg, setPinImg] = useState("");
  const [picImg, setPicImg] = useState("");

  // 핀이미지
  const eventImg = useAppSelector((state) => state.eventReducer.eventimageurl);
  // 사진찍은 이미지
  const picturImg = useAppSelector((state) => state.eventReducer.pictureimg);

  useEffect(() => {
    setPinImg(eventImg);
    setPicImg(picturImg);
  });

  return (
    <>
    <p className=" font-gmarket-thin text-center text-lg">두 사진이 포즈가 같나요?</p>
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
          src={`${picImg}`}
          alt="picImg"
          width={400}
          height={700}
          className="pt-[5%] h-[35vh] px-3"
        />
      </div>
      <div className="flex justify-center  pt-[5%] ">
        <button className="w-[50%] h-[2.5rem] absolute bottom-[10%] bg-brand-red rounded-md text-white font-gmarket-thin ">
          확인하기
        </button>
      </div>
    </>
  );
};

export default EventPose;
