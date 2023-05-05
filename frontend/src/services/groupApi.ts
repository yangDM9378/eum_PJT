import { multipartAuthApi, jsonAuthApi } from "@/libs/axiosConfig";
import { Group, GroupCodeResult } from "@/types/group";

// 그룹 생성하기
const createGroup = async (data: Group) => {
  await multipartAuthApi.post(`/groups`, data);
};

// 그룹 리스트 가져오기
const getGroupList = async () => {
  const {
    data: { result },
  } = await jsonAuthApi.get(`/groups`);
  return result;
};

// 그룹 참여하기
const enterGroup = async (groupCode: string): Promise<GroupCodeResult> => {
  const data = await jsonAuthApi.post(`/groups/code`, { groupCode });
  return data.data;
};

// 그룹 상세사항
const detailGroup = async (groupId: number) => {
  const {
    data: { result },
  } = await jsonAuthApi.get(`/groups/${groupId}`);
  return result;
};
export { createGroup, getGroupList, enterGroup, detailGroup };
