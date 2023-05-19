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

  function moveGroupCss(isOpen: boolean): string {
    return isOpen === true ? "bg-brand-pink" : "border-2 border-brand-pink";
  }
  return (
    <div className=" font-gmarket-thin text-[15px] ">
      <button
        className={`w-[35vw] h-[5vh] rounded-md  ${moveGroupCss(Isopen)}`}
        onClick={openModal}
      >
        그룹 만들기
      </button>
      <MakeGroupModal isOpen={Isopen} setIsOpen={setIsopen} />
    </div>
  );
};

export default MakeButton;
