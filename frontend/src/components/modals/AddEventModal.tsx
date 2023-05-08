import Modal from "react-modal";
import { useState } from "react";
import { useAppSelector } from "@/redux/hooks";
import axios from "axios";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createPin } from "@/services/pinApi";
import { useRouter } from "next/navigation";

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
  const eventType = useAppSelector((state) => state.coordsReducer.path);
  const coords = useAppSelector((state) => state.coordsReducer.coords);
  const groupId = useAppSelector((state) => state.coordsReducer.groupId);
  const queryClient = useQueryClient();
  const { mutate, isLoading } = useMutation(createPin, {
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["initial-map"] });
    },
  });

  const router = useRouter();

  const addEvent = async (e: { preventDefault: () => void }) => {
    e.preventDefault();
    const formData = new FormData();

    const jsonData = {
      title: title,
      content: content,
      latitude: coords.lat,
      longitude: coords.lng,
      type: eventType,
      groupId: groupId,
    };

    if (eventType === "aging") {
      const blobRes = await axios.get(image, { responseType: "blob" });
      formData.append("image", blobRes.data, "image.png");
    }
    if (eventType === "pose") {
      const blobRes = await (await fetch(image)).blob();
      formData.append("image", blobRes, "image.png");
    }

    await formData.append(
      "pinAddReq",
      new Blob([JSON.stringify(jsonData)], { type: "application/json" })
    );

    await mutate(formData);
    await router.push(`/map/${groupId}`);
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
            <button type="submit">
              {isLoading ? "등록 중..." : "등록하기"}
            </button>
          </label>
        </form>
      </section>
    </Modal>
  );
};

export default AddEventModal;
