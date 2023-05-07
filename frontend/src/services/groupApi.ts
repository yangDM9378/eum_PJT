import { multipartAuthApi, jsonAuthApi } from "@/libs/axiosConfig";

interface Result {
  result: null;
  resultCode: number;
  resultMsg: string;
}
import { Group, GroupCodeResult, GroupDetail } from "@/types/group";

// 그룹 생성하기
const createGroup = async (data: FormData) => {
  await multipartAuthApi.post(`/groups`, data);
};

// 그룹 리스트 가져오기
const getGroupList = async (): Promise<Array<Group>> => {
  const response = await jsonAuthApi.get(`/groups`);
  const groupList: Array<Group> = response.data.result;
  return groupList;
};

// 그룹 참여하기
const enterGroup = async (groupCode: string): Promise<GroupCodeResult> => {
  const data = await jsonAuthApi.post(`/groups/code`, { groupCode });
  return data.data;
};

// 그룹 상세사항
const detailGroup = async (groupId: number): Promise<GroupDetail> => {
  const response = await jsonAuthApi.get(`/groups/${groupId}`);
  const detailData: GroupDetail = response.data.result;
  return detailData;
};
export { createGroup, getGroupList, enterGroup, detailGroup };
