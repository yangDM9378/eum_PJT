"use client";

import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import { useRouter } from "next/navigation";
import { PindetailResult } from "@/types/pin";
import { getPinDetail } from "@/services/pinApi";
import { useAppDispatch } from "@/redux/hooks";
import { eventimageurl, eventtype } from "@/redux/doevent/eventSlice";

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
      {detailData && (
        <section className="relative flex flex-col px-2 py-3 text-center">
          <img
            src="/modal/closeBTN.png"
            alt="닫기버튼"
            className="absolute left-[95%] top-[0%]"
            onClick={() => setMessageOpen(false)}
          />
          <div className="py-3 text-xl">{detailData?.result.title}</div>
          <div className="text-sm">{detailData?.result.content}</div>
          <img
            src={process.env.NEXT_PUBLIC_IMAGE_URL + detailData.result.image}
            alt="이벤트사진"
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
      )}
    </Modal>
  );
};

export default MessageModal;
