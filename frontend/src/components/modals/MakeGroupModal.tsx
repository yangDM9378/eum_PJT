"use client";

import React from "react";
import { AiOutlineCamera } from "react-icons/ai";
import Modal from "react-modal";

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
    width: "85vw",
    height: "80vh",
    background: "#F8F9F3",
  },
};

type ModalProps = {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

const EventOptionModal = ({ isOpen, setIsOpen }: ModalProps) => {
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={() => {
        setIsOpen(false);
      }}
      ariaHideApp={false}
      style={customStyles}
    >
      <img
        src="/modal/closeBTN.png"
        alt="닫기버튼"
        className="absolute left-[90%] top-[5%]"
        onClick={() => setIsOpen(false)}
      />
      <div className="pt-[20%] flex flex-col items-center h-full">
        <span className="">만드는 모임 이름을 작성해주세요.</span>
        <input
          type="text"
          className="w-[80%] h-[10%] bg-transparent border border-brand-baige-2"
        />

        <p className="pt-[10%]">당신의 모임을 설명해 주세요.</p>
        <input
          type="text"
          className="w-[80%] h-[30%] pt-[10%] bg-transparent border border-brand-baige-2 "
        />

        <p className="pt-[10%]">모임의 사진을 올려주세요.</p>
        <AiOutlineCamera className="pt-[5%] w-[50px] h-[50px]" />
        <button className="bg-brand-red w-[70%] h-[10%] mt-[5%] font-gmarket-thin">
          만들기
        </button>
      </div>
    </Modal>
  );
};

export default EventOptionModal;
