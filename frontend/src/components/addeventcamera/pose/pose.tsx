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
    <>
      <div className="flex justify-center">
        <Image
          src={PoseImage}
          alt=""
          width={300}
          height={700}
          className="pt-[10%] h-[60vh] px-3"
        />
      </div>
      <div className="flex justify-center pt-[10%] ">
        <button onClick={addEventModalOpen}className="w-[60vw] rounded-md  bg-brand-blue h-[5vh]">
          저장
        </button>
        <AddEventModal
        modalOpen ={modalOpen}
        setModalOpen={setModalOpen}
        image={PoseImage}
        />
      </div>
    </>
  );
};

export default Pose;
