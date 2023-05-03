import Image from "next/image";
import React from "react";
import login from "../../../public/images/kakao_login.png";

export default function LoginButton() {
  const oauthlogin =
    process.env.NEXT_PUBLIC_OUATH_KAKAO_HOSTNAME +
    "?redirect_url=" +
    process.env.NEXT_PUBLIC_OUATH_KAKAO_REDIRECT_URL;

  return (
    <div className="flex justify-center pt-[40vh]">
      <a href={oauthlogin}>
        <Image className="w-[30vh] h-[7vh]" src={login} alt="로그인 버튼" />
      </a>
    </div>
  );
}
