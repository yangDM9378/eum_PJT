"use client";

import { useAppSelector } from "@/redux/hooks";
import React from "react";
import AddEventModal from "@/components/modals/AddEventModal";
import { useEffect, useState } from "react";
import Image from "next/image";

const Pose = () => {
  const [PoseImage, SetPoseImage] = useState("");

  const [modalOpen, setModalOpen] = useState(false);

  const addEventModalOpen = () => {
    setModalOpen(true);
  };

  const originImageUrl = useAppSelector(
    (state) => state.addEventReducer.originimageurl
  );

  useEffect(() => {
    SetPoseImage(originImageUrl);
  });
  return (
    <div className="flex flex-col items-center justify-center w-[100vw] h-[92vh]">
      {PoseImage && (
        <Image
          className="border rounded-lg border-brand-blue border-spacing-1 drop-hadow-2xl"
          src={PoseImage}
          alt="poseimage"
          width={340}
          height={400}
        />
      )}
      <div className="flex justify-center">
        <button
          onClick={addEventModalOpen}
          className="my-[2vh] bg-brand-blue text-white py-[1.5vh] px-[6vw] rounded-md shadow-xl font-brand-gmarketsans"
        >
          이벤트 등록
        </button>
        <AddEventModal
          modalOpen={modalOpen}
          setModalOpen={setModalOpen}
          image={PoseImage}
        />
      </div>
    </div>
  );
};

export default Pose;
