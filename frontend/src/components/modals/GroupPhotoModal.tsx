"use client";

import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import Image from "next/image";
import { getPinImage } from "@/services/galleryApi";
import { PictureDetail } from "@/types/picture";
import Script from "next/script";

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

  const [kakaoLoaded, setKakaoLoaded] = useState<boolean>(false);
  // 카카오 로딩 상태

  // 핀 이미지 불러오기
  const getPhotoDetail = async () => {
    console.log(pictureId,'?❔')
      const photoRes = await getPinImage(pictureId);
      setPhotoInfo(photoRes);
  
  };

  // 렌더링 되자마자 핀 이미지 불러오는 함수 실행
  useEffect(() => {
    getPhotoDetail();
  }, []);

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
    if (kakaoLoaded) {
      window.Kakao.init(process.env.NEXT_PUBLIC_KAKAO_API_KEY);
    }
  }, [kakaoLoaded]);

  // 버튼 클릭하면 카카오톡 공유하기 함수 실행
  const sharephoto = () => {
    window.Kakao.Share.sendDefault({
      objectType: "feed",
      content: {
        title: "오늘의 디저트",
        description: "아메리카노, 빵, 케익",
        imageUrl:
          "https://mud-kage.kakao.com/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
        link: {
          mobileWebUrl: "https://i-eum-u.com",
          webUrl: "https://i-eum-u.com",
        },
      },
    });
  };

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
