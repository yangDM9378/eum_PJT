"use client";

import React from "react";
import Modal from "react-modal";
type ModalProps = {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

const customStyles = {
  overlay: {
    background: "rgba(0, 0, 0, 0.5)",
  },
  content: {
    top: "40%",
    left: "50%",
    right: "auto",
    bottom: "auto",
    marginRight: "-50%",
    transform: "translate(-50%, -50%)",
    border: "none",
    width: "80vw",
    height: "35vh",
    background: "#F8F9F3",
  },
};

const EnterGroupModal = ({ isOpen, setIsOpen }: ModalProps) => {
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={() => {
        setIsOpen(false);
      }}
      ariaHideApp={false}
      style={customStyles}
    >
      <div className="flex flex-col items-center justify-center">
        <img
          src="/modal/closeBTN.png"
          alt="닫기버튼"
          className="absolute left-[90%] top-[5%]"
          onClick={() => setIsOpen(false)}
        />
        <p className="pt-[15%]">그룹 코드를 입력해 주세요.</p>
        <input
          type="text"
          className=" mt-[5%] pt-[10%] bg-transparent border border-brand-baige-2"
        />
        <button className="bg-brand-red w-[70%] h-[5vh] mt-[10%] font-gmarket-thin">
          들어가기
        </button>
      </div>
    </Modal>
  );
};

export default EnterGroupModal;
