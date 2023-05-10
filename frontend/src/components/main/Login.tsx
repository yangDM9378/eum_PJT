"use client";

import Image from "next/image";
import React, { useEffect, useState } from "react";
import login from "../../../public/images/kakao_login.png";
import useWindowSize from "@/libs/helper/useWindowSize";
import MapUpper from "./MainMapUpper";

import { useQuery } from "@tanstack/react-query";
import { getPinAll } from "@/services/pinApi";

const Main = () => {
  const getMainMap = async () => {
    const pinList = await getPinAll();
    return pinList;
  };

  const { data, isLoading } = useQuery({
    queryKey: ["initial-main-pin"],
    queryFn: async () => await getMainMap(),
  });

  const size = useWindowSize();

  const oauthlogin =
    process.env.NEXT_PUBLIC_OUATH_KAKAO_HOSTNAME +
    "?redirect_url=" +
    process.env.NEXT_PUBLIC_OUATH_KAKAO_REDIRECT_URL;

  //counting animation
  const [count, setCount] = useState(0);
  const rate = 200;

  const end = data ? data.length : 0;
  const start = 0;
  const duration = 3000;
  const frameRate = 1000 / 80;
  const totalFrame = Math.round(duration / frameRate);

  const easeOutExpo = (number: number) => {
    return number === 1 ? 1 : 1 - Math.pow(2, -10 * number);
  };
  useEffect(() => {
    let currentNumber = start;
    const counter = setInterval(() => {
      // 5️⃣ currentNumber / totalFrame 이 1에 가까워질수록 느리게 카운팅
      const progress = easeOutExpo(++currentNumber / totalFrame);
      if (rate) setCount(Math.round(end * progress));

      if (progress === 1) {
        clearInterval(counter);
      }
    }, frameRate);
  }, [end, frameRate, start, totalFrame]);

  return size ? (
    <div>
      {size > 450 ? (
        // 웹페이지 버전입니다.
        <section className="w-[100vw] h-[100vh] flex">
          <div className="w-[50%] m-auto h-[80%] flex flex-col">
            <div className="flex flex-col justify-center w-[70%] h-[100%] m-auto">
              <div className="text-4xl">이음</div>
              <div className="text-lg pt-5 pb-3">
                총 &nbsp;
                <span className="text-brand-blue text-xl">
                  {rate ? count : "0"}
                </span>
                개의 메시지가 남겨져있습니다.
              </div>
              <div className="text-lg">당신의 메시지를 남겨주세요</div>
              <a href="" className="h-[50%] w-[80%] relative">
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
          <MapUpper markerList={data} />
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
