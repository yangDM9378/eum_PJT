"use client";

import React from "react";
import Modal from "react-modal";
import { useRouter } from "next/navigation";

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
  const router = useRouter();
  const moveEvent = () => {
    router.push("/eventcamera");
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
        <img
          src="/modal/closeBTN.png"
          alt="닫기버튼"
          className="absolute left-[95%] top-[0%]"
          onClick={() => setMessageOpen(false)}
        />
        {/* <div>좌표 id : {messageId}</div> */}
        <div className="text-xl py-3">사진찍기 좋은 곳이에요.</div>
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
