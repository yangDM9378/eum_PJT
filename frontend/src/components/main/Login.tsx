"use client";

import Image from "next/image";
import React, { useEffect, useState } from "react";
import login from "../../../public/images/kakao_login.png";
import useWindowSize from "@/libs/helper/useWindowSize";
import MapUpper from "./MainMapUpper";



const Main = () => {
  const size = useWindowSize();


  const oauthlogin =
    process.env.NEXT_PUBLIC_OUATH_KAKAO_HOSTNAME +
    "?redirect_url=" +
    process.env.NEXT_PUBLIC_OUATH_KAKAO_REDIRECT_URL;

  return size ? (
    <div>
      {size > 450 ? (
        <section className="w-[100vw] h-[100vh]">
          <MapUpper />
        </section>
      ) : (
        <div
          className="w-[100vw] h-[100vh] max-w-[360px]:"
          style={{
            backgroundImage: "url(/images/main.png)",
            backgroundRepeat: "no-repeat",
            backgroundSize: "contain",
          }}
        >
          <p className="text-[35px] pt-[20vh] pl-[5vh]">이음</p>
          <div className="flex justify-center pt-[40vh]">
            <a href={oauthlogin}>
              <Image
                className="w-[30vh] h-[7vh]"
                src={login}
                alt="로그인 버튼"
              />
            </a>
          </div>
        </div>
      )}
    </div>
  ) : (
    <div>로딩중입니다.</div>
  );
};

export default Main;
