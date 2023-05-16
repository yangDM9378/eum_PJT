"use client";

import { useAppSelector } from "@/redux/hooks";
import Image from "next/image";
import { useEffect, useState } from "react";

const frameImg = () => {
  const [originImg, setOriginImg] = useState("");
  const frameImg = useAppSelector((state) => state.coordsReducer.frameImg);

  useEffect(() => {
    if (frameImg) {
      setOriginImg(frameImg);
    }
  }, []);

  return (
    <div className="h-[92vh] flex flex-col items-center justify-center">
      {originImg && (
        <div className="flex items-center justify-center border rounded-lg">
          <Image src={originImg} alt="frameImg" width={300} height={350} />
        </div>
      )}
    </div>
  );
};

export default frameImg;
