"use client";

import EnterGroupModal from "@/components/modals/EnterGroupModal";
import React from "react";
import { useState } from "react";

import InfoModal from '../../modals/InfoModal'


const EnterButton = () => {
  const [isOpen, setIsOpen] = useState<boolean>(false);

  const openModal = () => {
    setIsOpen(true);
  };

  return (
    <div className="pl-[3vw] font-brand-gmarketsans">
      <button
        onClick={openModal}
        className="bg-brand-pink w-[35vw] h-[3vh] rounded-md font-brand-gmarketsans text-[15px]"
      >
        그룹 들어가기
      </button>
      {/* <EnterGroupModal isOpen={isOpen} setIsOpen={setIsOpen} /> */}
      <InfoModal isOpen={isOpen} setIsOpen={setIsOpen}/>
    </div>
  );
};

export default EnterButton;
