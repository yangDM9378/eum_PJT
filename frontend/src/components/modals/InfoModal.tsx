"use client";


import React from "react";
import Modal from "react-modal";
import Image from "next/image";

type ModalProps = {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

const customStyles = {
  overlay: {
    background: "rgba(0, 0, 0, 0.7)",

   
    
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
    height: "40vh",
    background: "#F8F9F3"
    
  },
};

const GroupPhotoModal = ({ isOpen, setIsOpen }: ModalProps) => {
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
        <p className="font-gmarket-medium text-sm pt-[10%]">사진을 보고 포즈를 따라 찍어주세요</p>
        <div className="pt-[8%]">
        <Image src="/images/GroupSample.png" alt="" width={250} height={80}  className="rounded-lg h-[150px] drop-shadow-lg" />
        </div>
      </div>
    </Modal>
  );
};

export default GroupPhotoModal;
