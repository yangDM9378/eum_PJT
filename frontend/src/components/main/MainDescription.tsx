"use client";

import React, { useEffect, useState } from "react";
import { Pin } from "@/types/pin";
import useCountUp from "@/libs/helper/useCountNum";
import { makeQrCode } from "@/libs/helper/QrCodeLoder";

interface Props {
  markerList: Array<Pin> | undefined;
}

const MainDescription = ({ markerList }: Props) => {
  const [end, setEnd] = useState(0);
  const [isQr, setIsQr] = useState(false);
  const [qrCode, setQrCode] = useState(null);

  useEffect(() => {
    if (markerList) {
      setEnd(markerList.length);
    }
  }, [markerList]);
  const count = useCountUp(end);

  // Qr코드 부분
  useEffect(() => {
    makeQrCode(process.env.NEXT_PUBLIC_QR_CODE || "").then((data) => {
      setQrCode(data);
    });
  }, []);

  return (
    <div className="w-[50%] m-auto h-[80%] flex flex-col">
      <div className="flex flex-col justify-center w-[70%] h-[100%] m-auto">
        <div className="text-4xl">이음</div>
        <div className="pt-5 pb-3 text-lg">
          총 &nbsp;
          {markerList && (
            <span className="text-xl text-brand-blue">{count}</span>
          )}
          개의 메시지가 남겨져있습니다.
        </div>
        <div className="text-lg">당신의 메시지를 남겨주세요</div>

        <div
          className="h-[60%] w-[80%] flex flex-col p-[5%]"
          onClick={() => setIsQr(!isQr)}
        >
          {!isQr ? (
            <>
              <img
                src="/images/mail.png"
                alt="letter"
                className="flex h-[30vw] w-[30vw] justify-center items-center outline-none"
              />
              <p className="text-center text-brand-green">앱 다운로드 받기</p>
            </>
          ) : (
            <div className="flex items-center justify-center">
              <img
                src={qrCode || ""}
                alt="QR Code"
                className="m-[5vw] h-[15vw] w-[15vw] outline-none"
              />
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default MainDescription;
