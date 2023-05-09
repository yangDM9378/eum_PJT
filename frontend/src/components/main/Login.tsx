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
        // 웹페이지 버전입니다.
        <section className="w-[100vw] h-[100vh] flex">
          <div className="w-[50%] m-auto h-[80%] flex flex-col">
            <div className="flex flex-col justify-center w-[50%] h-[100%] m-auto">
              <div className="text-4xl">이음</div>
              <div className="text-lg pt-5 pb-3">
                총 <span className="text-brand-blue text-xl">100</span>개의
                메시지가 남겨져있습니다.
              </div>
              <div className="text-lg">당신의 메시지를 남겨주세요</div>
              <a href="" className="h-[50%] w-[87%] relative">
                <img
                  src="/images/mail.png"
                  alt=""
                  className="h-[100%] w-[100%]"
                />
                <div className="  text-brand-green absolute top-[87%] left-[35%]">
                  앱 다운로드 받기
                </div>
              </a>
            </div>
          </div>
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
