"use client";

import React from "react";
import Modal from "react-modal";

const customStyles = {
  content: {
    top: "50%",
    left: "50%",
    right: "auto",
    bottom: "auto",
    marginRight: "-50%",
    transform: "translate(-50%, -50%)",
  },
};

type ModalProps = {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

const EventOptionModal = ({ isOpen, setIsOpen }: ModalProps) => {
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={() => {
        setIsOpen(false);
      }}
      ariaHideApp={false}
      style={customStyles}
    >
      <div>hi</div>
    </Modal>
  );
};

export default EventOptionModal;
