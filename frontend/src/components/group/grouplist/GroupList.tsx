"use client";

import Image from "next/image";
import { useQuery, useQueryClient } from "@tanstack/react-query";

import { Poppins } from "next/font/google";
import { getGroupList } from "@/services/groupApi";
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
    queryFn: async () => await getGroup(),
  });
  const goToMap = async (groupId: number) => {
    await router.push(`/map/${groupId}`);
  };
  return (
    <>
      <ul className="pt-[2vh] ">
        {data &&
          data.map((group, index) => (
            <li key={index} onClick={() => goToMap(group.groupId)}>
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
                    src={`${process.env.NEXT_PUBLIC_IMAGE_URL}group/image/${group.image}`}
                    alt={group.name}
                    width={400}
                    height={400}
                  />
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
