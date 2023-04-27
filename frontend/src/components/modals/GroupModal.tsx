"use client";
import React from "react";

import {AiOutlineCamera} from 'react-icons/ai'


type ClickModalType = () => void;

const GroupModal = (props: { clickModal: ClickModalType }) => {
  const { clickModal } = props;

  return (
    <div className="h-[70vh] w-[80vw] bg-brand-baige absolute top-[20vh] rounded-[15px]">
      <div>
        <p>만드는 모임 이름을 작성해주세요</p>
        <input type="text" />
      </div>
      <div>
        <p>만드는 모임을 설명해 주세요.</p>
        <input type="text" />
      </div>
      <div>모임의 사진을 올려주세요</div>
      <AiOutlineCamera className="text-[50px]"/>
    </div>
  );
};

export default GroupModal;
