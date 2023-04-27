"use client";

import GroupModal from "@/components/modals/GroupModal";
import React from "react";
import { useState } from "react";


const MakeButton = () => {
    const [showModal, setShowModal] = useState<boolean>(false)
    const clickMaodal = () => setShowModal(!showModal)


  return (
    <div className="font-brand-gmarketsans">
      <button className="bg-brand-pink w-[35vw] h-[3vh] rounded-md font-brand-gmarketsans text-[15px]"
      onClick={clickMaodal}>
        그룹 만들기
      </button>
      {showModal && <GroupModal clickModal={clickMaodal}/>}
    </div>
  );
};




export default MakeButton;
