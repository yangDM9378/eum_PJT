"use client";

import { useEffect } from "react";
import { usePathname, useRouter } from "next/navigation";
import Cookies from "js-cookie";

const RouterGuard = ({ children }: { children: React.ReactNode }) => {
  const pathname = usePathname();
  const router = useRouter();
  const accesstoken = Cookies.get("accessToken");

  useEffect(() => {
    if (pathname !== "/" && accesstoken === undefined) {
      router.replace("/");
    }
  }, []);

  return <div>{children}</div>;
};

export default RouterGuard;
