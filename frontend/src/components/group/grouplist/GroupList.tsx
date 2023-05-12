"use client";
import { useEffect, useState } from "react";
import Image from "next/image";
import { useQuery, useQueryClient, useMutation } from "@tanstack/react-query";
import { GroupCodeResult } from "@/types/group";
import { Poppins } from "next/font/google";
import { deleteGroup, getGroupList, outGroup } from "@/services/groupApi";
import { useRouter } from "next/navigation";
import { AiOutlineClose } from "react-icons/ai";

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
      <ul className="pt-[1vh] h-[70vh] overflow-scroll">
        {data &&
          data.map((group, index) => (
            <li key={index}>
              <div className="flex flex-col w-[95%] mix-blend-saturation">
                <div className=" flex justify-end">
                  <AiOutlineClose onClick={() => outGroupList(group.groupId)} />
                </div>
                <div
                  className="  flex justify-between"
                  onClick={() => goToMap(group.groupId)}
                >
                  {/* 그룹 이미지 */}
                  <div className=" w-[30%]">
                    <img
                      className="rounded-sm h-[13vh] w-[13vh] m-1 "
                      src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${group.image}`}
                      alt={group.name}
                    />
                  </div>
                  {/* 모임 정보들 */}
                  <div className="w-[70%]">
                    <p className=" font-semibold text-[1rem]">{group.name}</p>
                    <p className="te xt-[0.8rem] font-normal pt-[1vh]">
                      {group.description}
                    </p>
                  </div>
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
