"use client";

import { useAppSelector } from "@/redux/hooks";
import Image from "next/image";

const EventAging = () => {
  const pictureImg = useAppSelector((state) => state.eventReducer.pictureimg);
  const eventImg = useAppSelector((state) => state.eventReducer.eventimageurl);
  return (
    <>
      <Image src={pictureImg} alt="pictureImg" width={220} height={240} />
      <Image
        src={process.env.NEXT_PUBLIC_IMAGE_URL + eventImg}
        alt="eventImg"
        width={220}
        height={240}
      />
    </>
  );
};

export default EventAging;
