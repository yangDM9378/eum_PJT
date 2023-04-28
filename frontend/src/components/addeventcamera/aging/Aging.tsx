"use client";

import axios from "axios";
import Image from "next/image";
import { useEffect, useState } from "react";
import { useAppSelector } from "@/redux/hooks";

const Aging = () => {
  const [originImage, setOriginImage] = useState<string>("");
  const [oldImage, setOldImage] = useState<string>("");
  const originImageUrl = useAppSelector(
    (state) => state.addEventReducer.originimageurl
  );
  useEffect(() => {
    setOriginImage(originImageUrl);
  }, []);

  // const startAging = async () => {
  //   const oldFormData = new FormData();
  //   // const kidFormData = new FormData();
  //   const blob = await (await fetch(removebgImageUrl)).blob();
  //   oldFormData.append("image", blob, "oldimage.png");
  //   oldFormData.append("action_type", "TO_OLD");

  //   const response = await axios.post(
  //     "https://www.ailabapi.com/api/portrait/effects/face-attribute-editing",
  //     oldFormData,
  //     {
  //       headers: {
  //         "Content-Type": "multipart/form-data",
  //         "ailabapi-api-key": process.env.NEXT_PUBLIC_AILAB_API_KEY,
  //       },
  //     }
  //   );
  //   setOldImage(response.data.result.image);
  // };

  // const test = () => {
  //   console.log(oldImage);
  // };

  return (
    <>
      <div>
        <button type="button">저장하기</button>
      </div>
      <div className="flex items-center justify-evenly">
        <Image
          className="border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
          src={originImage}
          alt="removebgimage"
          width={320}
          height={260}
        />
      </div>
      {/* <div className="flex items-center justify-center">
        <button
          className="my-[4vh] py-[1vh] px-[6vw] rounded-full border border-black border-spacing-2 shadow-xl font-brand-gmarketsans"
          type="button"
          onClick={startAging}
        >
          에이징 하기
        </button>
      </div>
      <div className="flex items-center justify-evenly">
        <Image
          className="border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
          src={`data:image/png;base64,${oldImage}`}
          alt="oldImage"
          width={320}
          height={260}
        />
      </div> */}
      {/* <button onClick={test}>test</button> */}
    </>
  );
};

export default Aging;
