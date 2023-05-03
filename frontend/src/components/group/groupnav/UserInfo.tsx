"use client";

import React, { useEffect } from "react";
import { getUser } from "@/services/userApi";

const UserInfo = () => {
  useEffect(() => {
    const response = getUser();
    console.log(response);
  }, []);

  return <div>UserInfo</div>;
};

export default UserInfo;
