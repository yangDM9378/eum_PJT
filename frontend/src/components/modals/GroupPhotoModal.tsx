"use client";

import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import Image from "next/image";
import { getPinImage } from "@/services/galleryApi";
import { PictureDetail } from "@/types/picture";

type ModalProps = {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
  pictureId: number;
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
    height: "60vh",
    background: "#F8F9F3",
  },
};

const GroupPhotoModal = ({ isOpen, setIsOpen, pictureId }: ModalProps) => {
  // 핀 이미지 상태

  const [photoInfo, setPhotoInfo] = useState<PictureDetail>();

  // 핀 이미지 불러오기
  const getPhotoDetail = async () => {
    const photoRes = await getPinImage(pictureId);
    console.log(pictureId,'👻👻')
    setPhotoInfo(photoRes);
    console.log(photoRes,'❔❔')
  };


  // 렌더링 되자마자 핀 이미지 불러오는 함수 실행
  useEffect(() => {
    getPhotoDetail();
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
            width={300}
            height={300}
            className="rounded-lg"
          />
        </div>
        <button className="bg-brand-green w-[50%] h-[5vh] mt-[10%] font-gmarket-thin rounded-xl">
          공유
        </button>
      </div>
    </Modal>
  );
};

export default GroupPhotoModal;
