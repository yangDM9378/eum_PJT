"use client";

import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import { useRouter } from "next/navigation";
import { PindetailResult } from "@/types/pin";
import { getPinDetail } from "@/services/pinApi";

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

  // messageId로 핀 상세 조회 데이터 가져오기
  useEffect(() => {
    // detailpin axios 호출부분
    if (messageOpen) {
      const getPinDetailData = async () => {
        const pinDetailRes = await getPinDetail(messageId);
        setDetailData(pinDetailRes);
      };
      getPinDetailData();
      console.log(detailData);
    }
  }, [messageId]);

  const router = useRouter();
  const moveEvent = async () => {
    // redux event 폴더 생성하고 detailpin에서 온 데이터 image type

    // 타입으로 `/eventcamera/${type}/`이동
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
      <section className="relative flex flex-col px-2 py-3 text-center">
        <img
          src="/modal/closeBTN.png"
          alt="닫기버튼"
          className="absolute left-[95%] top-[0%]"
          onClick={() => setMessageOpen(false)}
        />
        <div className="py-3 text-xl">사진찍기 좋은 곳이에요.</div>
        <div className="text-sm">젊은 시절의 나와 사진 찍어요!</div>
        <img
          src="/images/GroupSample.png"
          alt="예시사진"
          className="h-[25vh] my-4 rounded-[10px] shadow-xl"
        />
        <img
          src="/images/GroupSample.png"
          alt="예시사진"
          className="h-[25vh] mb-4 rounded-[10px] shadow-xl"
        />
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
