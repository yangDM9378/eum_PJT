"use client";

import Image from "next/image";
import React from "react";

const PoseImagePage = () => {
  return (
    <>
      <div className="flex justify-center h-[70%] pt-[3%]">
        <Image
          src={"/images/GroupSample.png"}
          alt=""
          width={300}
          height={150}
        />
      </div>
      <div className="flex justify-center  pt-[10%] ">
        <button className="w-[50%] h-[2.5rem] bg-brand-red rounded-md text-white font-gmarket-thin ">
          확인
        </button>
      </div>
    </>
  );
};

export default PoseImagePage;
