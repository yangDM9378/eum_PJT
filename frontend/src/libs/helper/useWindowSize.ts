"use client";

import { useLayoutEffect, useState } from "react";

const useWindowSize = () => {
  const [size, setSize] = useState<number | null>(null);

  useLayoutEffect(() => {
    const updateSize = () => {
      setSize(window.innerWidth);
      console.log("사이즈 변화");
    };
    updateSize();
    window.addEventListener("resize", updateSize);

    return () => window.removeEventListener("resize", updateSize);
  }, []);
  return size;
};

export default useWindowSize;
