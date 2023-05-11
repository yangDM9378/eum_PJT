"use client";
import { useEffect, useState } from "react";
import Image from "next/image";
import { useQuery, useQueryClient, useMutation } from "@tanstack/react-query";
import { GroupCodeResult } from "@/types/group";
import { Poppins } from "next/font/google";
import { deleteGroup, getGroupList, outGroup } from "@/services/groupApi";
import { useRouter } from "next/navigation";

const poppins = Poppins({
  weight: ["200", "300", "500"],
  preload: false,
});

const GroupList = () => {
  const router = useRouter();
  const queryClient = useQueryClient();
  const getGroup = async () => {
    const response = await getGroupList();
    return response;
  };

  const { data, isLoading, isFetching, error } = useQuery({
    queryKey: ["initial-group"],
    queryFn: getGroup,
  });

  const goToMap = async (groupId: number) => {
    await router.push(`/map/${groupId}`);
  };

  // 그룹 나가기
  const outGroupList = async (groupId: number) => {
    await outGroup(groupId);
    await queryClient.invalidateQueries({ queryKey: ["initial-group"] });
    alert("그룹에서 탈퇴하였습니다.");
  };

  return (
    <>
      <ul className="pt-[2vh] h-[70vh] overflow-scroll">
        {data &&
          data.map((group, index) => (
            <li key={index}>
              <div
                className="grid grid-cols-10 pl-[3vw] place-content-around py-3"
                onClick={() => goToMap(group.groupId)}
              >
                <div className="col-span-7 font-brand-poppins">
                  <p className="font-brand-poppins text-[1rem]">{group.name}</p>
                  <p className="text-[0.8rem] pt-[0.5vh] font-thin">
                    {group.description}
                  </p>
                </div>
                <div className="col-span-3 mr-[2vw] h-[13vh] w-[13vh]">
                  <img
                    className="rounded-sm w-[100%] h-[100%]"
                    src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${group.image}`}
                    alt={group.name}
                  />
                </div>
              </div>
              <div className="flex justify-end ">
                <div
                  onClick={() => outGroupList(group.groupId)}
                  className="bg-brand-red rounded text-sm p-1 m-2"
                >
                  그룹 나가기
                </div>
              </div>
              <hr className="border" />
            </li>
          ))}
      </ul>
    </>
  );
};

export default GroupList;
