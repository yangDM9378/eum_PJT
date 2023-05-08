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
import GroupPhotoModal from "./GroupPhotoModal";

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

// 메세지 모달
const MessageModal = ({
  messageOpen,
  setMessageOpen,
  messageId,
}: ModalProps) => {
  const [detailData, setDetailData] = useState<PindetailResult>();
  const dispatch = useAppDispatch();

  // 상세이미지 모달 상태
  const [isOpen, setIsOpen] = useState<boolean>(false);

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

  // 찍은 사진들 보여주기
  const [imagesUrls, setImagesUrls] = useState<[] | Picture[]>([]);

  // messageId로 핀 이미지들 불러오기
  const getpinImagesData = async (messageId: number) => {
    const images = await getpinImages(messageId);
    setImagesUrls(images);
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
  const [selectedIdx, setSelectedIdx] = useState<number>(0);

  // 선택된 이미지
  const [selectedImage, setSelectedImage] = useState<string | null>(null);

  // 이미지 선택하기
  const selecteimage = (id: number, image: string) => {
    setSelectedIdx(id);
    setSelectedImage(image);
  };

  // 메세지 모달 닫고 상세 이미지 모달 열기
  const CloseModal = () => {
    setMessageOpen(false);
    setIsOpen(true);
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
            src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${detailData.result.image}`}
            alt="이벤트사진"
            className="h-[25vh] my-4 rounded-[10px] shadow-xl"
          />
          <div className="flex flex-row justify-center mb-3">
            <div className="flex flex-col-reverse pr-7">
              {selectedImage === null ? (
                <div>Loading...</div>
              ) : (
                imagesUrls.map((image) => (
                  <img
                    key={image.pictureId}
                    src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${image.image}`}
                    alt=""
                    width={60}
                    height={60}
                    className="my-2"
                    onClick={() => selecteimage(image.pictureId, image.image)}
                  />
                ))
              )}
            </div>
            {selectedImage !== null && (
              <img
                src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${selectedImage}`}
                alt="선택된 이미지"
                height={150}
                width={150}
                className="rounded-lg"
                onClick={() => {
                  CloseModal();
                }}
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
      <GroupPhotoModal
        isOpen={isOpen}
        selectedIdx={selectedIdx}
        setIsOpen={setIsOpen}
      />
    </Modal>
  );
};

export default MessageModal;
