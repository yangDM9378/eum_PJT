"use client";

import React, { useEffect, useState } from "react";
import { Pin } from "@/types/pin";
import useCountUp from "@/libs/helper/useCountNum";

interface Props {
  markerList: Array<Pin> | undefined;
}

const MainDescription = ({ markerList }: Props) => {
  const [end, setEnd] = useState(0);
  useEffect(() => {
    if (markerList) {
      setEnd(markerList.length);
    }
  }, [markerList]);
  const count = useCountUp(end);

  return (
    <div className="w-[50%] m-auto h-[80%] flex flex-col">
      <div className="flex flex-col justify-center w-[70%] h-[100%] m-auto">
        <div className="text-4xl">이음</div>
        <div className="text-lg pt-5 pb-3">
          총 &nbsp;
          {markerList && (
            <span className="text-brand-blue text-xl">{count}</span>
          )}
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
