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
    <div className="text-[15px] pr-2 font-gmarket-thin flex justify-center items-center ">
      <button
        onClick={openModal}
        className={`p-2
                  text-xs 
                  rounded-md
                   bg-transparent border-2 
                    border-brand-red
                     hover:bg-brand-red
                      active:bg-brand-red
                      focus:bg-brand-red
                     font-gmarket-thin `}
      >
        함께 꾸미기
      </button>
      <CodyCodeModal isOpen={isOpen} setIsOpen={setIsOpen} />
    </div>
  );
};

export default CodyButton;
