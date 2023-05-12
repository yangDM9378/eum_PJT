"use client";
import { stopCamera } from "@/utils/getCamera";
import { useRouter } from "next/navigation";
import BackIcon from "./common/BackIcon";

const Nav = () => {
  const router = useRouter();
  const goBack = () => {
    router.back();
  };

  return (
    <div className="h-[8%] text-black">
      <button onClick={goBack}>
        <BackIcon />
      </button>
    </div>
  );
};

export default Nav;
