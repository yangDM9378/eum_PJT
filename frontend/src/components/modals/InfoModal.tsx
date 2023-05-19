"use client";

import React, { useEffect } from "react";
import Modal from "react-modal";
import Image from "next/image";
import { useAppSelector } from "@/redux/hooks";
import { useState } from "react";

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
    background: "#F8F9F3",
  },
};

const GroupPhotoModal = ({ isOpen, setIsOpen }: ModalProps) => {
  //리덕스에 있는 등록한 이벤트 사진 가져오기
  const poseImage = useAppSelector((state) => state.eventReducer.eventimageurl);

  const [originPoseImage, setOriginPoseImage] = useState<string>("");

  useEffect(() => {
    if (poseImage) {
      setOriginPoseImage(poseImage);
    }
  }, []);
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={() => {
        setIsOpen(false);
      }}
      ariaHideApp={false}
      style={customStyles}
    >
      <div className="flex flex-col items-center h-full justify-evenly">
        <img
          src="/modal/closeBTN.png"
          alt="닫기버튼"
          className="absolute left-[85%] top-[8%]"
          onClick={() => setIsOpen(false)}
        />
        <p className="font-gmarket-medium text-m py-[2vh]">
          포즈를 따라 찍어주세요
        </p>
        <div>
          <Image
            src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${originPoseImage}`}
            alt="이벤트 이미지"
            width={300}
            height={200}
            className="rounded-lg drop-shadow-lg"
          />
        </div>
      </div>
    </Modal>
  );
};

export default GroupPhotoModal;
