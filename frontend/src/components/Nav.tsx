"use client";
import { usePathname } from "next/navigation";

const Nav = () => {
  const pathname = usePathname();
  const excludePath = ["/addeventcamera"];

  if (excludePath.includes(pathname)) {
    return null;
  }

  return <div className="h-[8%]">내비게이션바입니다.</div>;
};

export default Nav;
