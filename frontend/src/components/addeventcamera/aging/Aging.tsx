"use client";

import axios from "axios";
import Image from "next/image";
import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { useRouter } from "next/navigation";
import { agingselecturl } from "@/redux/addevent/addEventSlice";
import { agingApi } from "@/services/agingAPi";

const Aging = () => {
  const [originImage, setOriginImage] = useState<string>("");
  const [oldImage, setOldImage] = useState<string>("");
  const [kidImage, setKidImage] = useState<string>("");
  const [selectedimage, setSelectedImage] = useState<string>("");
  const dispatch = useAppDispatch();
  const router = useRouter();
  // 리덕스에 있는 오리지날 이미지 가져오기
  const originImageUrl = useAppSelector(
    (state) => state.addEventReducer.originimageurl
  );
  // 페이지가 시작될때 오리지날 이미지 저장
  useEffect(() => {
    setOriginImage(originImageUrl);
  }, []);

  const startAging = async () => {
    setSelectedImage(originImageUrl);
    const { oldImage, kidImage } = await agingApi(originImage);
    setOldImage(oldImage);
    setKidImage(kidImage);
  };

  // 에이징된 이미지를 선택하기
  const imgClick = (e: { target: any }) => {
    const src = e.target.src;
    setSelectedImage(src);
  };

  // aging된 사진중 원하는 사진 골라서 removebg로 이동하기
  const goRemovebg = () => {
    dispatch(agingselecturl(selectedimage));
    router.push(`/addeventcamera/aging/removebg`);
  };

  return (
    <div className="flex flex-col items-center justify-center w-full min-h-full">
      {originImage ? (
        <div>
          <Image
            className="border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
            src={originImage}
            alt="originImage"
            width={320}
            height={290}
          />
        </div>
      ) : (
        <div>사진을 찍으로 이동해주세요</div>
      )}

      {oldImage && kidImage ? (
        <div>
          <div className="mt-[5vh] flex items-center justify-center">
            <Image
              className="mr-[10px] border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
              src={selectedimage}
              alt="selectedimage"
              width={220}
              height={240}
            />
            <div className="flex flex-col">
              <Image
                className="border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
                src={`data:image/png;base64,${oldImage}`}
                alt="oldImage"
                width={80}
                height={80}
                onClick={imgClick}
              />
              <Image
                className="border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
                src={originImage}
                alt="originImage"
                width={80}
                height={80}
                onClick={imgClick}
              />
              <Image
                className="border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
                src={`data:image/png;base64,${kidImage}`}
                alt="kidImage"
                width={80}
                height={80}
                onClick={imgClick}
              />
            </div>
          </div>
          <div className="flex items-center justify-center">
            <button
              className="my-[4vh] bg-brand-blue text-white py-[1vh] px-[6vw] rounded-md  shadow-xl font-brand-gmarketsans"
              type="button"
              onClick={goRemovebg}
            >
              배경지우기
            </button>
          </div>
        </div>
      ) : (
        <button
          className="my-[4vh] py-[1vh] px-[6vw] rounded-full border border-black border-spacing-2 shadow-xl font-brand-gmarketsans"
          type="button"
          onClick={startAging}
        >
          에이징 하기
        </button>
      )}
    </div>
  );
};

export default Aging;
