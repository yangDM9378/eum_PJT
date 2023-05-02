"use client";

import React from "react";
import { useState } from "react";

import MakeGroupModal from "../../modals/MakeGroupModal";
import GroupPhotoModal from '../../modals/GroupPhotoModal'


const MakeButton = () => {
  const [Isopen, setIsopen] = useState<boolean>(false);

  // 클릭하면 모달 열기
  const openModal = () => {
    setIsopen(true);
  };

  return (
    <div className="font-brand-gmarketsans">
      <button
        className="bg-brand-pink w-[35vw] h-[3vh] rounded-md font-brand-gmarketsans text-[15px]"
        onClick={openModal}
      >
        그룹 만들기
      </button>
      {/* <MakeGroupModal isOpen={Isopen} setIsOpen={setIsopen} /> */}
      <GroupPhotoModal isOpen = {Isopen} setIsOpen={setIsopen}/>
    </div>
  );
};

export default MakeButton;
