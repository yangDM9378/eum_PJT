"use client";

import Image from "next/image";
import React, { useEffect, useState } from "react";
import login from "../../../public/images/kakao_login.png";
import useWindowSize from "@/libs/helper/useWindowSize";
import MapUpper from "./MainMapUpper";
import Loading from "../common/Loading";
import { AiOutlineCloud } from "react-icons/ai";
import { BsFillTreeFill } from "react-icons/bs";

import { useQuery } from "@tanstack/react-query";
import { getPinAll } from "@/services/pinApi";
import MainDescription from "./MainDescription";
import { Pin } from "@/types/pin";

const Main = () => {
  const [data, setData] = useState<undefined | Pin[]>(undefined);
  const getMainMap = async () => {
    const pinList = await getPinAll();
    setData(pinList);
  };

  useEffect(() => {
    getMainMap();
  }, []);

  const size = useWindowSize();

  const oauthlogin =
    process.env.NEXT_PUBLIC_OUATH_KAKAO_HOSTNAME +
    "?redirect_url=" +
    process.env.NEXT_PUBLIC_OUATH_KAKAO_REDIRECT_URL;

  return size ? (
    <div>
      {size > 450 ? (
        // 웹페이지 버전입니다.
        <section className="w-[100vw] h-[100vh] flex">
          <MainDescription markerList={data} />
          <MapUpper markerList={data} />
        </section>
      ) : (
        <div className="w-[100vw] h-[92%] bg-brand-baige">
          <hr className="w-[100vw] border-brand-blue absolute top-[10%]" />
          <AiOutlineCloud className="text-[30px] mt-[20%] ml-[20%] text-brand-blue" />
          <AiOutlineCloud className="text-[30px] ml-[70%] text-brand-blue" />
          <div className=" h-[100px] w-full overflow-hidden ">
            <div className="animate-show">
              <div
                className=" first_text color: bg-brand-baige pt-[10%] h-[10vh] w-[100vw] 
              text-[33px]
               py-[20%] px-[15%]
               inline-block 
              font-gmarket-thin"
              >
                추억을 이어주다
              </div>
            </div>
            <div>
              <div
                className=" second_text color: bg-brand-baige h-[10vh] w-[70vw] text-[40px]  px-[20%]  
              inline-block font-gmarket-thin"
              >
                이음
              </div>
            </div>
          </div>
          <hr className="w-[100vw] h-[2px] bg-brand-red absolute top-[40%]"></hr>
          <div className="absolute top-[55%] left-[50%] translate-x-[-50%]">
            <a href={oauthlogin}>
              <Image
                className="w-[30vh] h-[7vh]"
                src={login}
                alt="로그인 버튼"
              />
            </a>
          </div>
          <hr className="w-[100vw] h-[2px] bg-brand-green absolute bottom-[15%]"></hr>
        </div>
      )}
    </div>
  ) : (
    <div className="w-[100vw] h-[100vh] flex items-center justify-center">
      <Loading />
    </div>
  );
};

export default Main;
