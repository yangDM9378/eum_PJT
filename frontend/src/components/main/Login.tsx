"use client";

import Image from "next/image";
import React, { useEffect, useState } from "react";
import login from "../../../public/images/kakao_login.png";
import useWindowSize from "@/libs/helper/useWindowSize";
import MapUpper from "./MainMapUpper";
import Loading from "../common/Loading";

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
        <div className="w-[100vw] h-[100vh] bg-brand-baige max-w-[360px]:">
          <div className="w-[100vw] h-[2px] bg-brand-blue mt-[10%]"></div>
          <p className="text-[35px] pt-[20vh] pl-[8vh] font-gmarket-thin">
            이음
          </p>
          <div className="w-[100vw] h-[2px] bg-brand-red mt-[20%]"></div>
          <div className="flex justify-center pt-[30vh]">
            <a href={oauthlogin}>
              <Image
                className="w-[30vh] h-[7vh]"
                src={login}
                alt="로그인 버튼"
              />
            </a>
          </div>
          <div className="w-[100vw] h-[2px] bg-brand-green mt-[20%]"></div>
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
