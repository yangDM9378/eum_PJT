"use client";

import React from "react";
import { useState } from "react";

import MakeGroupModal from "../../modals/MakeGroupModal";

const MakeButton = () => {
  const [Isopen, setIsopen] = useState<boolean>(false);

  // 클릭하면 모달 열기
  const openModal = () => {
    setIsopen(true);
  };

  return (
    <div className="font-brand-gmarketsans">
      <button
        className="bg-brand-pink w-[35vw] rounded-md font-brand-gmarketsans text-[15px] h-[5vh]"
        onClick={openModal}
      >
        그룹 만들기
      </button>
      <MakeGroupModal isOpen={Isopen} setIsOpen={setIsopen} />
    </div>
  );
};

export default MakeButton;
