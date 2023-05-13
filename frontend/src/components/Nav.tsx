"use client";
import { stopCamera } from "@/utils/getCamera";
import { usePathname, useRouter } from "next/navigation";
import BackIcon from "./common/BackIcon";

const Nav = () => {
  const router = useRouter();
  const pathName = usePathname();
  const goBack = () => {
    router.back();
  };
  const exPathName = ["/addeventcamera", "/eventcamera"];
  return !exPathName.includes(pathName) ? (
    <div className="h-[8%] text-black">
      <button onClick={goBack}>
        <BackIcon />
      </button>
    </div>
  ) : (
    <div></div>
  );
};

export default Nav;
