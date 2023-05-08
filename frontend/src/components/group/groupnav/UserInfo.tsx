"use client";

import React, { useEffect, useState } from "react";
import { getUser } from "@/services/userApi";
import { Usertype } from "@/types/user";

const UserInfo = () => {
  const [userData, setUserData] = useState<Usertype | null>(null);
  const getUserData = async () => {
    const response = await getUser();
    setUserData(response);
  };

  useEffect(() => {
    getUserData();
  }, []);

  return (
    <div>
      <p className="font-brand-gmarketsans text-[30px]">
        {userData && userData.name}
      </p>
    </div>
  );
};

export default UserInfo;
