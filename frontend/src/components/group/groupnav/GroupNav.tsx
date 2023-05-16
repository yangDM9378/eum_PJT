import React from "react";
import CodyButton from "./CodyButton";
import EnterButton from "./EnterButton";
import MakeButton from "./MakeButton";
import UserInfo from "./UserInfo";

export default function groupnav() {
  return (
    <div className="bg-brand-red h-[30vh] flex flex-col justify-center">
      <div className="flex flex-row  text-white text-2xl h-[30%] justify-center items-center">
        <UserInfo />
        <p className="font-brand-gmarketsans font-light w-[50%] ml-[1%]">
          님과 이어진 모임
        </p>
      </div>
      <div className="flex flex-row justify-center mt-[2%]">
        <MakeButton />
        <EnterButton />
        <CodyButton />
      </div>
    </div>
  );
}
