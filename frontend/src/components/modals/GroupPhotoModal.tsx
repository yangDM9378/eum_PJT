"use client";

import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import Image from "next/image";
import { getPinImage } from "@/services/galleryApi";
import { PictureDetail } from "@/types/picture";
import GroupInfo from "../map/GroupInfo";
import { Result } from "postcss";
import { isConstructorDeclaration } from "typescript";

type ModalProps = {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
  messageOpen: boolean;
  selected: number;
  setSelected: React.Dispatch<React.SetStateAction<number>>;
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

const GroupPhotoModal = ({
  isOpen,
  setIsOpen,
  selected,
  setSelected,
}: ModalProps) => {
  // 핀 이미지 상태

  const [photoInfo, setPhotoInfo] = useState<PictureDetail>();

  // const [photoURL, setPhoURL] = useState<string| undefined>('')

  const [kakaoLoaded, setKakaoLoaded] = useState<boolean>(false);
  // 카카오 로딩 상태

  // 핀 이미지 불러오기
  const getPhotoDetail = async () => {
    const photoRes = await getPinImage(selected);
    await setPhotoInfo(photoRes);
  };

  useEffect(() => {
    const script = document.createElement("script");
    script.src = "https://developers.kakao.com/sdk/js/kakao.min.js";
    script.async = true;
    document.head.appendChild(script);

    script.onload = () => {
      setKakaoLoaded(true);
    };
  }, []);

  useEffect(() => {
    getPhotoDetail();
  }, [selected]);

  useEffect(() => {
    if (kakaoLoaded) {
      window.Kakao.init(process.env.NEXT_PUBLIC_KAKAO_API_KEY);
    }
  }, [kakaoLoaded]);

  // 버튼 클릭하면 카카오톡 공유하기 함수 실행
  const sharephoto = async () => {
    await window.Kakao.Share.sendDefault({
      objectType: "feed",
      content: {
        title: `${photoInfo?.userName}이 찍은 사진`,
        description: `${photoInfo?.createdDate}`,
        imageUrl: `${process.env.NEXT_PUBLIC_IMAGE_URL}${photoInfo?.image}`,
        link: {
          mobileWebUrl: "https://i-eum-u.com",
          webUrl: "https://i-eum-u.com",
        },
      },
    });
    setSelected(0);
  };

  // 닫기를 누르면 함수 호출
  const closeModal = () => {
    setIsOpen(false);
    setSelected(0);
  };




  return (
    <Modal
      isOpen={isOpen}
      // onRequestClose={() => {
      //   setIsOpen(false);
      // }}
      ariaHideApp={false}
      style={customStyles}
    >
      <div className="flex flex-col items-center justify-center">
        <img
          src="/modal/closeBTN.png"
          alt="닫기버튼"
          className="absolute left-[90%] top-[5%]"
          onClick={() => closeModal()}
        />
        <div className="pt-[10%]">
          {photoInfo && (
            <Image
              src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${photoInfo?.image}`}
              alt=""
              width={300}
              height={300}
              className="rounded-lg"
            />
          )}
        </div>
        <button
          className="bg-brand-green w-[50%] h-[5vh] mt-[10%] font-gmarket-thin rounded-xl"
          onClick={sharephoto}
        >
          공유
        </button>
      </div>
    </Modal>
  );
};

export default GroupPhotoModal;
