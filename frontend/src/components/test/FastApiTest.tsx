"use client";

import { getFastApiData } from "@/services/poseApi";
import { useEffect, useState } from "react";
const FastApiTest = () => {
  const [testData, setTestData] = useState<string | null>("");

  useEffect(() => {
    const fastapidata = async () => {
      const data = await getFastApiData();
      setTestData(data?.message);
    };
    fastapidata();
  }, []);

  const click = () => {
    console.log(testData);
  };

  return (
    <div>
      <button onClick={click}>111</button>
    </div>
  );
};

export default FastApiTest;
