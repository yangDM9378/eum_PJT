"use client";

import React, { useCallback, useState } from "react";
import Modal from "react-modal";
import { useMutation, useQuery } from "@tanstack/react-query";
import { enterGroup } from "@/services/groupApi";
import axios, { AxiosResponse } from "axios";

type ModalProps = {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
};


interface Result {
  result: null;
  resultCode: string;
  resultMsg: string;
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
    height: "35vh",
    background: "#F8F9F3",
  },
};


const EnterGroupModal = ({ isOpen, setIsOpen }: ModalProps) => {
  // 그룹코드
  const [groupCode, setgroupCode] = useState<string>("");

  // 코드 응답
  const [response, setResponse] = useState<Result>({
    result: null,
    resultCode: '',
    resultMsg: "",
  });


  // API 응답 데이터를 상태로 저장
  const handleSuccess = (data: Result) => {
    setResponse(data); 
  };

  //useMutation 타입 순서대로 응답 타입, 오류 타입, 보내는 값 타입
  const enterGroupMutation = useMutation(enterGroup, {
    onSuccess: (data) => {
      handleSuccess(data);
    },
  });

  // 그룹코드를 입력하면 서버에 axios 보내는 코드
  const enterCode = () => {
    enterGroupMutation.mutate(groupCode);
  };

  // 코드를 입력해서 setgroupCode에 저장하는
  const onchange = (event: React.FormEvent<HTMLInputElement>) => {
    setgroupCode(event.currentTarget.value);
    console.log(groupCode, "🎈🎈");
  };

  // 응답 코드가 Created이면 모달 닫기
  if (response.resultCode === 'Created') {
    setIsOpen(false);
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
        <p className="pt-[15%]">그룹 코드를 입력해 주세요.</p>
        <input
          type="text"
          className=" mt-[5%] pt-[10%] bg-transparent border border-brand-baige-2"
          onChange={onchange}
        />
        <button
          type="button"
          onClick={enterCode}
          className="bg-brand-red w-[70%] h-[5vh] mt-[10%] font-gmarket-thin"
        >
          들어가기
        </button>
        {response && <p>API Response: {JSON.stringify(response)}</p>}
      </div>
    </Modal>
  );
};

export default EnterGroupModal;
