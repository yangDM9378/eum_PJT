"use client";

import CodyCodeModal from "@/components/modals/CodyCodeModal";
import React from "react";
import { useState } from "react";

const CodyButton = () => {
  const [isOpen, setIsOpen] = useState<boolean>(false);

  const openModal = () => {
    setIsOpen(true);
  };

  function moveGroupCss(isOpen: boolean): string {
    return isOpen === true ? "bg-brand-pink" : "border-2 border-brand-pink";
  }

  return (
    <div className="pl-[5vw] text-[15px]  font-gmarket-thin ">
      <button
        onClick={openModal}
        className={`w-[35vw] h-[5vh] rounded-lg ${moveGroupCss(isOpen)}`}
      >
        함께 꾸미기
      </button>
      <CodyCodeModal isOpen={isOpen} setIsOpen={setIsOpen} />
    </div>
  );
};

export default CodyButton;
