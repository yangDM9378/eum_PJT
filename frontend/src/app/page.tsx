import Main from "../components/main/Login";

// 전역으로 카카오 존재를 정의
declare global {
  interface Window {
    Kakao: any;
  }
}
export default function Home() {
  return (
    <>
      <Main />
    </>
  );
}

export {};
