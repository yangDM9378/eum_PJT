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
import { pictureid } from "@/redux/doevent/messageSlice";
import { getPinImage } from "@/services/galleryApi";
import { useQuery } from "@tanstack/react-query";

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
  setIsPhotoOpen: React.Dispatch<React.SetStateAction<boolean>>;
  setSelected: React.Dispatch<React.SetStateAction<number>>;
};

// 메세지 모달
const MessageModal = ({
  messageOpen,
  setMessageOpen,
  messageId,
  setIsPhotoOpen,
  setSelected,
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

  // 찍은 사진들 보여주기
  // const [imagesUrls, setImagesUrls] = useState<[] | Picture[]>([]);

  // messageId로 핀 이미지들 불러오기
  const getpinImagesData = async (messageId: number) => {
    const images = await getpinImages(messageId);
    return images;
    // await setImagesUrls(images);
  };
  const { data, isLoading } = useQuery(["initial-pinpicture", messageId], () =>
    getpinImages(messageId)
  );

  // useEffect(() => {
  //   if (data && data.length > 0) {
  //     setSelectedImage(data[0].image);
  //     setSelectedIdx(data[0].pictureId);
  //   } else {
  //     setSelectedImage("");
  //   }
  // }, [data]);

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

  const initialInfo = {
    name: "",
    time: "",
  };

  // 선택된 이미지
  const [selectedImage, setSelectedImage] = useState<string | null>(null);
  const [selectedInfo, setSelectedInfo] = useState(initialInfo);

  const getPinData = async (id: number) => {
    const data = await getPinImage(id);
    const dataDate = new Date(data.createdDate);
    const newData = {
      name: data.userName,
      time: dataDate.toDateString(),
    };
    setSelectedInfo(newData);
  };

  // 이미지 선택하기
  const selecteimage = (id: number, image: string) => {
    getPinData(id);
    // pictureId 값을 넣어줘요.
    setSelectedIdx(id);
    setSelectedImage(image);
  };

  // 메세지 모달 닫고 상세 이미지 모달 열기
  const CloseModal = async () => {
    setSelected(selectedIdx);

    setMessageOpen(false);
    setIsPhotoOpen(true);
  };

  // useEffect(() => {
  //   dispatch(pictureid(selectedIdx));
  // }, [selectedIdx]);

  return (
    <Modal
      isOpen={messageOpen}
      // onRequestClose={() => {
      //   setMessageOpen(false);
      // }}
      ariaHideApp={false}
      style={customStyles}
    >
      {detailData && (
        <section className="relative flex flex-col px-2 py-3 text-center">
          <img
            src="/modal/closeBTN.png"
            alt="닫기버튼"
            className="absolute left-[95%] top-[0%]"
            onClick={() => {
              setMessageOpen(false);
              setSelectedImage(null);
            }}
          />
          <div className="py-3 text-xl">{detailData?.result.title}</div>
          <div className="text-sm">{detailData?.result.content}</div>
          <img
            src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${detailData.result.image}`}
            alt="이벤트사진"
            className="h-[25vh] my-4 rounded-[10px] shadow-xl border-2 border-brand-blue"
          />
          <div className="flex flex-row justify-center mb-3 max-h-[30vh]">
            <div className="relative flex flex-col-reverse overflow-y-scroll ">
              {data?.length === 0 ? (
                <p className="flex text-lg">아직 함께 찍은 사진이 없어요😭</p>
              ) : (
                data?.map((image) => (
                  <img
                    key={image.pictureId}
                    src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${image.image}`}
                    alt=""
                    width={70}
                    height={60}
                    className={`min-h-[10vh] my-[5%] mr-[5vw] ${
                      selectedIdx === image.pictureId
                        ? "border-4 border-brand-red"
                        : ""
                    }`}
                    onClick={() => selecteimage(image.pictureId, image.image)}
                  />
                ))
              )}
            </div>

            {selectedImage !== null ? (
              <div>
                <img
                  src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${selectedImage}`}
                  alt="선택된 이미지"
                  height={270}
                  width={200}
                  className="rounded-lg h-[25vh] ml-[5%] my-auto"
                  onClick={() => {
                    CloseModal();
                  }}
                />
                <div className="font-gmarket-thin text-[12px] text-right mt-[2%]">
                  from {selectedInfo.name}
                </div>
                <div className="font-gmarket-thin text-[8px] text-right ">
                  {selectedInfo.time}
                </div>
              </div>
            ) : (
              data?.length !== 0 && (
                <div className="w-[150px] h-[150px] border-2 border-brand-blue rounded-md m-auto">
                  {" "}
                </div>
              )
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
