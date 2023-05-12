"use client";

import React, { useEffect, useRef, useState } from "react";
import { AiOutlineCamera } from "react-icons/ai";
import Modal from "react-modal";
import { createGroup } from "@/services/groupApi";
import { useMutation, useQueryClient } from "@tanstack/react-query";

interface Result {
  result: null;
  resultCode: string;
  resultMsg: string;
}

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
    width: "85vw",
    height: "80vh",
    background: "#F8F9F3",
  },
};

type ModalProps = {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

const initialGroupState = {
  name: "",
  description: "",
  image: "",
};
const EventOptionModal = ({ isOpen, setIsOpen }: ModalProps) => {
  const [groupState, setGroupState] = useState(initialGroupState);
  const [uploadImg, setUploadImg] = useState<Blob | string>("");

  // 그룹 생성하기
  const makeGroup = () => {
    console.log(groupState);
    const formData = new FormData();
    const jsonData = {
      name: groupState.name,
      description: groupState.description,
    };
    formData.append("image", uploadImg, "image.png");
    formData.append(
      "groupAddReq",
      new Blob([JSON.stringify(jsonData)], { type: "application/json" })
    );
    makeGroupMutation.mutate(formData);
  };

  // 응답 코드
  const [response, setResponse] = useState<Result | null>(null);

  // 응답 성공 or 실패 분기
  useEffect(() => {
    if (response?.resultCode === "Created") {
      setGroupState(initialGroupState);
      setIsOpen(false);
    }
  });

  // 요청 성공시
  const handleSuccess = (data: Result) => {
    setResponse(data);
  };

  const queryClient = useQueryClient();

  // useMutation
  const makeGroupMutation = useMutation(createGroup, {
    onSuccess: (data) => {
      handleSuccess(data);
      queryClient.invalidateQueries({ queryKey: ["initial-group"] });
    },
  });

  // 그룹 이름 & 설명 내용 가져오기
  const handleChange = (e: { target: { name: string; value: string } }) => {
    const { name, value } = e.target;
    setGroupState({ ...groupState, [name]: value });
  };

  // 그룹 이미지 가져오기
  const groupImg = useRef<HTMLInputElement>(null);
  const saveGroupImg = async () => {
    if (groupImg?.current?.files !== null) {
      const file = groupImg?.current?.files[0];
      console.log(file);
      if (file) {
        setUploadImg(file);
        setGroupState({ ...groupState, image: URL.createObjectURL(file) });
      }
    }
  };

  // 모달 닫기
  const modalClose = () => {
    setIsOpen(false);
    setGroupState(initialGroupState);
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
      <img
        src="/modal/closeBTN.png"
        alt="닫기버튼"
        className="absolute left-[90%] top-[5%]"
        onClick={modalClose}
      />
      <div className="pt-[10%] flex flex-col items-center h-full">
        <span className="py-[5%]">만드는 모임 이름을 작성해주세요.</span>
        <input
          type="text"
          name="name"
          value={groupState.name}
          onChange={handleChange}
          className="w-[80%] h-[10%] bg-transparent border border-brand-baige-2"
        />
        <p className="py-[5%]">당신의 모임을 설명해 주세요.</p>
        <textarea
          name="description"
          value={groupState.description}
          onChange={handleChange}
          className="w-[80%] h-[30%] bg-transparent border border-brand-baige-2 resize-none	"
        />

        <p className="py-[5%]">모임의 사진을 올려주세요.</p>
        <label
          htmlFor="groupImg"
          className="w-[80%] h-[30%] flex items-center justify-center"
        >
          {groupState.image ? (
            <img
              src={groupState.image}
              alt=""
              className=" w-[70%] h-[90%] rounded-sm"
            />
          ) : (
            <AiOutlineCamera className=" w-[50px] h-[50px]" />
          )}
        </label>
        <input
          type="file"
          accept="image/*"
          id="groupImg"
          onChange={saveGroupImg}
          ref={groupImg}
          className="hidden"
        />
        <button
          className="bg-brand-red w-[70%] h-[10%] mt-[5%] font-gmarket-thin"
          onClick={makeGroup}
        >
          {makeGroupMutation.isLoading ? "그룹 생성 중" : "등록하기"}
        </button>
      </div>
    </Modal>
  );
};

export default EventOptionModal;
