"use client";

import { getFastApiData } from "@/services/poseApi";
import { useEffect, useState } from "react";
const FastApiTest = () => {
  const [testData, setTestData] = useState<string | null>("");

  useEffect(() => {
    const fastapidata = async () => {
      const response = await getFastApiData();
      if (response) {
        setTestData(response);
      }
    };
    fastapidata();
  }, []);

  return <div>111</div>;
};

export default FastApiTest;
