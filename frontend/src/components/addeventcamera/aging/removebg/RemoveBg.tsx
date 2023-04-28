"use client";

import Image from "next/image";
import { useEffect, useState } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";
import { useAppSelector } from "@/redux/hooks";

const RemoveBg = () => {
  const [agingImage, setAgingImage] = useState("");
  const [removebgImageUrl, setRemovebgImageUrl] = useState("");

  // redux에 있는 선택된 에이징 base64 파일 가져오기
  const agingSelectUrl = useAppSelector(
    (state) => state.addEventReducer.agingselecturl
  );

  useEffect(() => {
    fetchBlob();
  }, []);

  // base64파일 blob으로 만들어서 배경지우기 요청
  const fetchBlob = async () => {
    const formData = new FormData();
    console.log(agingImage);
    console.log(agingSelectUrl);
    const blob = await (await fetch(agingSelectUrl)).blob();
    formData.append("image", blob, "image.png");
    const response = await axios.post(
      "https://www.ailabapi.com/api/cutout/general/universal-background-removal",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
          "ailabapi-api-key": process.env.NEXT_PUBLIC_AILAB_API_KEY,
        },
      }
    );
    setAgingImage(agingSelectUrl);
    setRemovebgImageUrl(response.data.data.image_url);
  };

  return (
    <>
      {agingImage && removebgImageUrl && (
        <div className="flex flex-col items-center justify-center w-full min-h-full">
          <Image
            className="border rounded-lg border-brand-blue border-spacing-1 drop-shadow-2xl"
            src={agingImage}
            alt="agingImage"
            width={320}
            height={260}
          />
          <Image
            className="border rounded-lg border-brand-blue border-spacing-1 drop-shadow-2xl"
            src={removebgImageUrl}
            alt="removebgimage"
            width={320}
            height={260}
          />
          <button
            className="bg-brand-blue text-white py-[1vh] px-[6vw] rounded-md  shadow-xl font-brand-gmarketsans"
            type="button"
          >
            이벤트등록
          </button>
        </div>
      )}
    </>
  );
};

export default RemoveBg;
