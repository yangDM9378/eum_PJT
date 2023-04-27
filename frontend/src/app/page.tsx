// import { Inter } from "next/font/google";
// const inter = Inter({ subsets: ["latin"] });

import main from "../../public/images/main.png";
// import Image from "next/image";
import LoginButton from "../components/main/Login";

export default function Home() {
  return (
    <div
      className="w-[100vw] h-[100vh] max-w-[360px]:"
      style={{
        backgroundImage: "url(/images/main.png)",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
      }}
    >
      <p className="font-brand-gmarketsans text-[35px] pt-[20vh] pl-[5vh]">이음</p>
      <LoginButton />
    </div>
  );
}
