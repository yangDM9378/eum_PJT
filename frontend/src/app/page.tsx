// import { Inter } from "next/font/google";
// const inter = Inter({ subsets: ["latin"] });

import main from "../../public/images/main.png";
// import Image from "next/image";
import LoginButton from "@/components/main/login";

export default function Home() {
  return (
    <div
      className="w-[100vw] h-[100vh]"
      style={{
        backgroundImage: "url(/images/main.png)",
        backgroundRepeat: "no-repeat",

        backgroundSize: "contain",
      }}
    >
      <p className="font-gmarketsans text-[45px] pt-[20vh] pl-[5vh]">이음</p>
      <LoginButton />
    </div>
  );
}
