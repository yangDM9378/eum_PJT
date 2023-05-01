"use client";

import React from "react";
import { AiOutlineCamera } from "react-icons/ai";
import { AiOutlineClose } from "react-icons/ai";

import Modal from "react-modal";
type ModalProps = {
  isOpen: boolean;
  onRequestClose: () => void;
};

const ModalTest = ({ isOpen, onRequestClose }: ModalProps) => {
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      style={{
        overlay: {
          position: "fixed",
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: "rgba(255, 255, 255, 0.75)",
        },
        content: {
          background: "#F8F9F3",
          outline: "none",
          height: "70vh",
          width: "90vw",
          position: "absolute",
          border: "none",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          borderRadius: "25px",
        },
      }}
    >
      <div>
        <AiOutlineClose
          onClick={onRequestClose}
          className="text-[20px] absolute top-[2vh] right-[2vw]"
        />
        <div className="pt-[3vh]">
          <p>만드는 모임 이름을 작성해주세요</p>
          <input type="text" />
        </div>
        <div>
          <p>만드는 모임을 설명해 주세요.</p>
          <input type="text" />
        </div>
        <div>모임의 사진을 올려주세요</div>
        <AiOutlineCamera className="text-[50px]" />
      </div>
    </Modal>
  );
};

export default ModalTest;
