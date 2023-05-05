"use client";

import Image from "next/image";
import { useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";

import { Poppins } from "next/font/google";
import { getGroupList } from "@/services/groupApi";

const poppins = Poppins({
  weight: ["200", "300", "500"],
  preload: false,
});

// 타입
type Group = {
  id: string;
  name: string;
  description: string;
  image: string;
};

const GroupList = () => {
  const [groupList, setGroupList] = useState([]);
  const getGroup = async () => {
    const response = await getGroupList();
    console.log(response);
  };

  // const { data, isLoading, isFetching, error } = useQuery({
  //   queryKey: ["initial-group"],
  //   queryFn: async () => await getGroup(),
  // });
  // console.log(error);
  // const groupList: Group[] = [
  //   {
  //     id: "1",
  //     name: "사랑하는 엄마 아빠",
  //     description: "사랑하는 엄마아빠랑 여행 추억 남기는 곳",
  //     image: "GroupSample.png",
  //   },
  //   {
  //     id: "2",
  //     name: "삼형제와 함께",
  //     description: "말썽꾸러기들의 추억을 남겨보겠다",
  //     image: "GroupSample.png",
  //   },
  //   {
  //     id: "3",
  //     name: "사랑하는 엄마 아빠",
  //     description: "사랑하는 엄마아빠랑 여행 추억 남기는 곳",
  //     image: "GroupSample.png",
  //   },
  // ];

  return (
    <>
      <ul className="pt-[2vh] ">
        {/* {groupList.map((group, index) => (
          <li key={index}>
            <div className="grid grid-cols-10 pl-[3vw] place-content-around py-3">
              <div className="col-span-8 font-brand-poppins">
                <p className="font-brand-poppins text-[1rem]">{group.name}</p>
                <p className="text-[0.8rem] pt-[0.5vh] font-thin">
                  {group.description}
                </p>
              </div>
              <div className="col-span-2 mr-[2vw]">
                <Image
                  className="rounded-sm"
                  src={`/images/${group.image}`}
                  alt={group.name}
                  width={400}
                  height={400}
                />
              </div>
            </div>
            <hr className="border" />
          </li>
        ))} */}
      </ul>
    </>
  );
};

export default GroupList;
