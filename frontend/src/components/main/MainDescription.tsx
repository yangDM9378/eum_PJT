"use client";

import React, { useEffect, useState } from "react";
import { Pin } from "@/types/pin";

interface Props {
  markerList: Array<Pin> | undefined;
}
const MainDescription = ({ markerList }: Props) => {
  //counting animation
  const [count, setCount] = useState(0);
  const rate = 200;

  const end = markerList ? markerList.length : 0;
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

  return (
    <div className="w-[50%] m-auto h-[80%] flex flex-col">
      <div className="flex flex-col justify-center w-[70%] h-[100%] m-auto">
        <div className="text-4xl">이음</div>
        <div className="text-lg pt-5 pb-3">
          총 &nbsp;
          <span className="text-brand-blue text-xl">{rate ? count : "0"}</span>
          개의 메시지가 남겨져있습니다.
        </div>
        <div className="text-lg">당신의 메시지를 남겨주세요</div>
        <a href="" className="h-[50%] w-[80%] relative">
          <img src="/images/mail.png" alt="" className="h-[100%] w-[100%]" />
          <div className="  text-brand-green absolute top-[87%] left-[35%]">
            앱 다운로드 받기
          </div>
        </a>
      </div>
    </div>
  );
};

export default MainDescription;
