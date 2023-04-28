"use client";

import Image from "next/image";
import { useEffect, useState } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";

const RemoveBg = () => {
  useEffect(() => {
    // redux에 있는 선택된 에이징 base64 파일 가져오기
    // // 배경제거 POST 요청 보내기
    // const formData = new FormData();
    // const blob = await (await fetch(originImageUrl)).blob();
    // formData.append("image", blob, "image.png");
    // const response = await axios.post(
    //   "https://www.ailabapi.com/api/cutout/general/universal-background-removal",
    //   formData,
    //   {
    //     headers: {
    //       "Content-Type": "multipart/form-data",
    //       "ailabapi-api-key": process.env.NEXT_PUBLIC_AILAB_API_KEY,
    //     },
    //   }
    // );
    // setRemovebgImageUrl(response.data.data.image_url);
  }, []);

  return (
    <>
      <div className="flex items-center justify-center">
        {/* <Image
          className="rounded-lg drop-shadow-2xl"
          src={originImageUrl}
          alt="originimage"
          width={320}
          height={260}
        /> */}
      </div>
      <div className="flex items-center justify-center">
        <div>
          {/* <Image
            className="border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
            src={removebgImageUrl}
            alt="removebgimage"
            width={320}
            height={260}
          /> */}
        </div>
      </div>
    </>
  );
};

export default RemoveBg;
