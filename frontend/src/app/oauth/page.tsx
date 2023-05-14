"use client";
import { useEffect } from "react";
import Cookies from "js-cookie";
import { useRouter } from "next/navigation";

const OauthPage = () => {
  const router = useRouter();
  //쿠키에서 토큰 가져오는 부분
  useEffect(() => {
    if (Cookies.get("accessToken")) {
      // const accesstoken = Cookies.get("accessToken");
      router.push("/group");
    }
  }, []);
  useEffect(() => {
    // Prefetch the dashboard page
    router.prefetch("/group");
  }, []);
  return <></>;
};
export default OauthPage;
