"use client";

import { useState } from "react";
import Modal from "react-modal";
import { useRouter } from "next/navigation";

type ModalProps = {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

const customStyles = {
  overlay: {
    background: "rgba(0, 0, 0, 0.5)",
  },
  content: {
    top: "50%",
    left: "50%",
    right: "auto",
    bottom: "auto",
    marginRight: "-50%",
    transform: "translate(-50%, -50%)",
    border: "none",
    width: "90vw",
    background: "#F8F9F3",
  },
};

const CodyCodeModal = ({ isOpen, setIsOpen }: ModalProps) => {
  const [inputCode, setInputCode] = useState("");
  const router = useRouter();

  const enterCode = () => {
    router.replace(`/soket/${inputCode}`);
  };
  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={() => {
        setIsOpen(false);
      }}
      ariaHideApp={false}
      style={customStyles}
    >
      <section className=" relative">
        <img
          src="/modal/closeBTN.png"
          alt="닫기버튼"
          className="absolute left-[95%] top-[-13%]"
          onClick={() => setIsOpen(false)}
        />
        <p className="pt-[15%]">그룹 코드를 입력해 주세요.</p>
        <input
          type="text"
          className=" mt-[5%] pt-[10%] bg-transparent border border-brand-baige-2"
          onChange={(e) => setInputCode(e.target.value)}
        />
        <button
          type="button"
          onClick={enterCode}
          className="bg-brand-red w-[70%] h-[5vh] mt-[10%] font-gmarket-thin"
        >
          들어가기
        </button>
      </section>
    </Modal>
  );
};

export default CodyCodeModal;
