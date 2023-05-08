"use client";

import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import { useRouter } from "next/navigation";
import Image from "next/image";
import { eventimageurl, eventtype } from "@/redux/doevent/eventSlice";
import { getPinDetail } from "@/services/pinApi";
import { PindetailResult } from "@/types/pin";
import { useAppDispatch } from "@/redux/hooks";

const customStyles = {
  overlay: {
    background: "rgba(0, 0, 0, 0.5)",
  },
  content: {
    top: "50%",
    left: "50%",
    right: "auto",
    bottom: "auto",
    marginRight: "-50%",
    transform: "translate(-50%, -50%)",
    border: "none",
    width: "90vw",
    background: "#F8F9F3",
  },
};

const images: string[] = [
  "/images/gallery1.png",
  "/images/gallery2.png",
  "/images/gallery3.png",
];


type ModalProps = {
  messageOpen: boolean;
  messageId: number;
  setMessageOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

const MessageModal = ({
  messageOpen,
  setMessageOpen,
  messageId,
}: ModalProps) => {
  const [detailData, setDetailData] = useState<PindetailResult>();
  const dispatch = useAppDispatch();
  // messageId로 핀 상세 조회 데이터 가져오기
  useEffect(() => {
    // detailpin axios 호출부분
    if (messageOpen) {
      const getPinDetailData = async () => {
        const pinDetailRes = await getPinDetail(messageId);
        setDetailData(pinDetailRes);
      };
      getPinDetailData();
    }
  }, [messageId]);

  const router = useRouter();
  const moveEvent = async () => {
    if (detailData) {
      dispatch(eventtype(detailData.result.type));
      dispatch(eventimageurl(detailData.result.image));
    }
    await router.push("/eventcamera");
  };
  return (
    <Modal
      isOpen={messageOpen}
      onRequestClose={() => {
        setMessageOpen(false);
      }}
      ariaHideApp={false}
      style={customStyles}
    >
      <section className="flex flex-col  py-3 px-2 relative text-center">
      <div className="flex flex-col">
          <img
            src="/images/GroupSample.png"
            alt="예시사진"
            className="h-[25vh] my-4 rounded-[10px] shadow-xl"
          />
          <div className="flex flex-row justify-center">
            <div className="flex flex-col flex-col-reverse pr-7">
              {images.map((image: string, id: number) => (
                <Image
                  key={id}
                  src={image}
                  alt=""
                  width={50}
                  height={50}
                  className="my-2"
                />
              ))}
            </div>
            <img
              src="/images/GroupSample.png"
              alt="예시사진"
              className="h-[30vh] rounded-[10px] shadow-xl mb-2"
            />
          </div>
        </div>

        <div
          className="bg-brand-green rounded-[5px] text-center text-lg py-2 shadow-xl "
          onClick={moveEvent}
        >
          함께 찍기
        </div>
      </section>
    </Modal>
  );
};

export default MessageModal;
