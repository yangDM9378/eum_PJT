"use client";

import Image from "next/image";
import { useEffect, useState } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";

const RemoveBg = () => {
  const [originImageUrl, setOriginImageUrl] = useState<
    string | undefined | null
  >(undefined);
  const [removebgImageUrl, setRemovebgImageUrl] = useState<
    string | undefined | null
  >(undefined);
  const router = useRouter();

  useEffect(() => {
    // localstorage에 있는 값 받아오기
    const originImageURL = localStorage.getItem("originimagepath");
    setOriginImageUrl(originImageURL);
  }, []);

  // 누끼따주세요 POST 요청 보내기
  const removebgclick = async () => {
    if (!originImageUrl) {
      return;
    }

    const formData = new FormData();
    const blob = await (await fetch(originImageUrl)).blob();
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
    setRemovebgImageUrl(response.data.data.image_url);
  };

  // 에이징 페이지로 이동
  const goAging = () => {
    console.log(removebgImageUrl);
    if (removebgImageUrl) {
      localStorage.setItem("removeimagepath", removebgImageUrl);
    }
    router.push(`/addcamera/removeBg/aging`);
  };

  return (
    <>
      <div className="flex items-center justify-center">
        {originImageUrl ? (
          <Image
            className="rounded-lg drop-shadow-2xl"
            src={originImageUrl}
            alt="originimage"
            width={320}
            height={260}
          />
        ) : (
          <div>사진을 다시 찍어주세요</div>
        )}
      </div>
      <div className="flex items-center justify-center">
        <button
          className="my-[4vh] py-[1vh] px-[6vw] rounded-full border border-black border-spacing-2 shadow-xl font-brand-gmarketsans"
          type="button"
          onClick={removebgclick}
        >
          배경지우기
        </button>
      </div>
      <div className="flex items-center justify-center">
        {removebgImageUrl ? (
          <div>
            <Image
              className="border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
              src={removebgImageUrl}
              alt="removebgimage"
              width={320}
              height={260}
            />
            <div className="flex items-center justify-center">
              <button
                className="my-[4vh] py-[1vh] px-[6vw] rounded-md bg-brand-blue shadow-xl font-brand-gmarketsans text-white"
                type="button"
                onClick={goAging}
              >
                에이징 필터로
              </button>
            </div>
          </div>
        ) : (
          <div>사진이 맘에 드시면 누끼 따기 버튼을 눌러 주세요</div>
        )}
      </div>
    </>
  );
};

export default RemoveBg;
