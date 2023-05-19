"use client";

import React, { useEffect, useState } from "react";
import { getUser } from "@/services/userApi";
import { Usertype } from "@/types/user";
import { useAppDispatch } from "@/redux/hooks";
import { assignName } from "@/redux/user/userSlice";

const UserInfo = () => {
  const dispatch = useAppDispatch();

  const [userData, setUserData] = useState<Usertype | null>(null);
  const getUserData = async () => {
    const response = await getUser();
    setUserData(response);
    if (response) {
      dispatch(assignName(response.name));
    }
  };

  useEffect(() => {
    getUserData();
  }, []);

  return (
    <div>
      <p className="text-[30px] font-gmarket-thin">
        {userData && userData.name}
      </p>
    </div>
  );
};

export default UserInfo;
