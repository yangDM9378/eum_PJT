"use client";
import { usePathname } from "next/navigation";
import BackIcon from "./common/BackIcon";

const Nav = () => {
  return (
    <div className="h-[8%] text-black">
      <BackIcon />
    </div>
  );
};

export default Nav;
