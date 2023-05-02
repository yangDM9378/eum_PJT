import Image from "next/image";
import React from "react";
import login from "../../../public/images/kakao_login.png";

export default function LoginButton() {
  return (
    <div className="flex justify-center pt-[40vh]">
      <Image className="w-[30vh] h-[7vh]" src={login} alt="로그인 버튼" />
    </div>
  );
}
