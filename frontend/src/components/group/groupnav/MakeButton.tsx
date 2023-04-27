"use client";



import React from "react";
import { useState } from "react";
import ModalTest from "@/components/modals/ModalTest";

const MakeButton = () => {
    const [modalIsopen, setModalIsOpen] = useState<boolean>(false)
    const clickMaodal = () => setModalIsOpen(!modalIsopen)

    const closeModal = () => setModalIsOpen(false);

  return (
    <div className="font-brand-gmarketsans">
      <button className="bg-brand-pink w-[35vw] h-[3vh] rounded-md font-brand-gmarketsans text-[15px]"
      onClick={clickMaodal}>
        그룹 만들기
      </button>
      {modalIsopen && <ModalTest isOpen={modalIsopen} onRequestClose={closeModal}/>}
    </div>
  );
};




export default MakeButton;
