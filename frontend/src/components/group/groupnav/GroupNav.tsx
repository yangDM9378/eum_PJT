import React from "react";
import EnterButton from "./EnterButton";
import MakeButton from "./MakeButton";
import { getUser } from "../../../services/userApi";

export default function groupnav() {
  const response = getUser();
  console.log(response);

  return (
    <div className="bg-brand-red h-[30vh]">
      <div className="flex flex-row pt-[10vh] pl-[10vw] text-white text-2xl ">
        <p className="font-brand-gmarketsans text-[30px]">최유경</p>
        <p className="font-brand-gmarketsans font-light">님과 이어진 모임</p>
      </div>
      <div className="flex flex-row pt-[1vh] pl-[10vw]">
        <MakeButton />
        <EnterButton />
      </div>
    </div>
  );
}
