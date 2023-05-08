"use client";

import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import { useRouter } from "next/navigation";
import { PindetailResult } from "@/types/pin";
import { getPinDetail } from "@/services/pinApi";
import { useAppDispatch } from "@/redux/hooks";
import { eventimageurl, eventtype } from "@/redux/doevent/eventSlice";
import Image from "next/image";
import { getpinImages } from "@/services/galleryApi";
import { Picture } from "@/types/picture";

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

  const [imagesUrl, setImagesUrl] = useState<[] | Picture[]>([]);

  // messageId로 핀 이미지들 불러오기
  const getpinImagesData = async (messageId: number) => {
    const images = await getpinImages(messageId);
    setImagesUrl(images);
    console.log(images[0]);
    setSelectedImage(images[0].image);
  };

  useEffect(() => {
    getpinImagesData(messageId);
  }, [messageId]);

  const router = useRouter();
  const moveEvent = async () => {
    if (detailData) {
      dispatch(eventtype(detailData.result.type));
      dispatch(eventimageurl(detailData.result.image));
    }
    await router.push("/eventcamera");
  };

  // 선택된 이미지 인덱스
  const [selectedidx, setSelectedIdx] = useState<number>(0);

  // 선택된 이미지
  const [selectedImage, setSelectedImage] = useState<string>("");

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
            src={`${process.env.NEXT_PUBLIC_IMAGE_URL}group/image/${detailData.result.image}`}
            alt="이벤트사진"
            className="h-[25vh] my-4 rounded-[10px] shadow-xl"
          />
          <div className="flex flex-row justify-center mb-3">
            <div className="flex flex-col flex-col-reverse pr-7">
              {imagesUrl &&
                imagesUrl.map((image) => (
                  <img
                    key={image.pictureId}
                    src={`${process.env.NEXT_PUBLIC_IMAGE_URL}group/image/${image.image}`}
                    alt=""
                    width={60}
                    height={60}
                    className="my-2"
                    onClick={() => setSelectedImage(image.image)}
                  />
                ))}
            </div>
            {selectedImage && (
              <img
                src={`${process.env.NEXT_PUBLIC_IMAGE_URL}group/image/${selectedImage}`}
                alt="선택된 이미지"
                height={150}
                width={150}
                className="rounded-lg"
              />
            )}
          </div>

          <div
            className="bg-brand-green rounded-[5px] text-center text-lg py-2 shadow-xl"
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
