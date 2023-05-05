"use client";

<<<<<<< HEAD
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
=======
import React, { useEffect } from "react";
import { getUser } from "@/services/userApi";

const UserInfo = () => {
  useEffect(() => {
    const response = getUser();
    console.log(response);
  }, []);

  return <div>UserInfo</div>;
>>>>>>> 88e159f3778d1163d3abd4f77c1660f42a571cea
};

export default UserInfo;
