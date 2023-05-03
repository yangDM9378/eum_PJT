import Modal from "react-modal";
import { useState } from "react";
import { useAppSelector } from "@/redux/hooks";
import axios from "axios";
import { useMutation } from "@tanstack/react-query";
import { createPin } from "@/services/pinApi";

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

type ModalProps = {
  modalOpen: boolean;
  setModalOpen: React.Dispatch<React.SetStateAction<boolean>>;
  image: string;
};

const AddEventModal = ({ modalOpen, setModalOpen, image }: ModalProps) => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const type = useAppSelector((state) => state.coordsReducer.path);
  const coords = useAppSelector((state) => state.coordsReducer.coords);

  const { mutate } = useMutation(createPin);

  const addEvent = async (e: { preventDefault: () => void }) => {
    e.preventDefault();
    const blobRes = await axios.get(image, { responseType: "blob" });

    const formData = new FormData();
    const json = {
      title: title,
      content: content,
      latitude: coords.lat.toString(),
      longitude: coords.lng.toString(),
    };
    formData.append("pinAddReq", json.toString());
    // formData.append("title", title);
    // formData.append("content", content);
    // formData.append("latitude", coords.lat.toString());
    // formData.append("longitude", coords.lng.toString());
    // formData.append("type", type);
    formData.append("image", blobRes.data, "image.png");
    //수정 바람
    // formData.append("group_id", "1");
    await mutate(formData);
  };
  return (
    <Modal
      isOpen={modalOpen}
      onRequestClose={() => {
        setModalOpen(false);
      }}
      ariaHideApp={false}
      style={customStyles}
    >
      <section className="relative flex flex-col px-2 py-3 text-center">
        <img
          src="/modal/closeBTN.png"
          alt="닫기버튼"
          className="absolute left-[95%] top-[0%]"
          onClick={() => setModalOpen(false)}
        />
        <form onSubmit={addEvent}>
          <label>
            <div>편지 제목을 작성해주세요.</div>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </label>
          <br />
          <label>
            <div>편지내용을 작성해주세요.</div>
            <textarea
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
            <br />
            <button type="submit">등록하기</button>
          </label>
        </form>
      </section>
    </Modal>
  );
};

export default AddEventModal;
