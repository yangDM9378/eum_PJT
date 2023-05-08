"use client";

import React from "react";
import Modal from "react-modal";
import Image from "next/image";

type ModalProps = {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
  selectedIdx: number;
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
    height: "50vh",
    background: "#F8F9F3",
  },
};

const GroupPhotoModal = ({ isOpen, setIsOpen, selectedIdx }: ModalProps) => {
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
        <div className="pt-[10%]">
          <Image
            src="/images/GroupSample.png"
            alt=""
            width={400}
            height={400}
            className="rounded-lg"
          />
        </div>
        <button className="bg-brand-green w-[30%] h-[5vh] mt-[10%] font-gmarket-thin rounded-xl">
          공유
        </button>
      </div>
    </Modal>
  );
};

export default GroupPhotoModal;
