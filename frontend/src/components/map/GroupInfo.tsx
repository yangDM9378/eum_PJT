"use client";

import { detailGroup } from "@/services/groupApi";
import { useEffect, useState } from "react";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import GroupCodeModal from "../modals/GroupCodeModal";
import { useRouter } from "next/navigation";
import CodyButton from "../group/groupnav/CodyButton";
interface Props {
  groupId: number;
}
const GroupInfo = ({ groupId }: Props) => {
  const [isOpen, setIsOpen] = useState(false);

  const getDetailGroup = async () => {
    const response = await detailGroup(groupId);
    return response;
  };

  const queryClient = useQueryClient();

  useEffect(() => {
    queryClient.invalidateQueries({ queryKey: ["initial-groupInfo"] });
  }, [groupId]);

  const { data, isLoading } = useQuery({
    queryKey: ["initial-groupInfo"],
    queryFn: getDetailGroup,
  });

  const handleCopyClipBoard = async (text: string) => {
    if ((window as any).Android) {
      (window as any).Android.copyToClipboard(text);
    } else {
      const clipboardPermission = await navigator.permissions.query({
        name: "clipboard-write" as PermissionName,
      });
      if (clipboardPermission.state === "granted") {
        await navigator.clipboard.writeText(text);
        setIsOpen(true);
      } else {
        console.log("");
      }
    }
  };
  const router = useRouter();
  const goToGallery = async (groupId: number) => {
    await router.push(`/groupgallery/${groupId}`);
  };

  return (
    <section className="h-[25%] flex ">
      {data && (
        <div className="flex justify-center items-center w-[90%] h-[80%] m-auto shadow-xl rounded-lg bg-white">
          <div className="w-[30%] h-[80%] flex">
            <img
              src={`${process.env.NEXT_PUBLIC_IMAGE_URL}${data.image}`}
              className="w-[80%] h-[80%] m-auto rounded-xl"
            />
          </div>
          <div className="w-[70%] h-[80%] flex flex-col justify-center">
            <div className="flex justify-between items-center">
              <div className="py-2 text-[90%] font-bold">{data.name}</div>
              <div className="p-1 mr-3 " onClick={() => goToGallery(groupId)}>
                <img src="/map/gallery.png" alt="mapgallery" />
              </div>
            </div>
            <div className="py-2 text-xs">{data.description}</div>
            <div className="flex px-2 w-[100%] justify-end">
              <CodyButton />
              <div
                className="p-2
                  text-xs 
                  rounded-md
                   bg-transparent border-2 
                    border-brand-red
                     hover:bg-brand-red
                      active:bg-brand-red
                      focus:bg-brand-red
                     font-gmarket-thin"
                onClick={() => {
                  handleCopyClipBoard(data.groupCode);
                }}
              >
                그룹 코드
              </div>
            </div>
          </div>
        </div>
      )}
      {data && <GroupCodeModal isOpen={isOpen} setIsOpen={setIsOpen} />}
    </section>
  );
};

export default GroupInfo;
