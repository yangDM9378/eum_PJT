"use client";

import { eventimageurl } from "@/redux/doevent/eventSlice";
import { useAppSelector } from "@/redux/hooks";
import Image from "next/image";
import React, { useEffect, useState } from "react";

const EventPose = () => {
  const [PoseImage, setPoseImage] = useState("");

  const originImageUrl = useAppSelector(
    (state) => state.eventReducer.eventimageurl
  );

  useEffect(() => {
    setPoseImage(originImageUrl);
  });

  return (
    <>
      <div className="flex justify-center h-[60%] pt-[3%]">
        <Image
          src={PoseImage}
          alt=""
          width={400}
          height={700}
          className="pt-[10%] h-[50vh] px-3"
        />
      </div>
      <div className="flex justify-center  pt-[5%] ">
        <button className="w-[50%] h-[2.5rem] bg-brand-red rounded-md text-white font-gmarket-thin ">
          확인
        </button>
      </div>
    </>
  );
};

export default EventPose;
