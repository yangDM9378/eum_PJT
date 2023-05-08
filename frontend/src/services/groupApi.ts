import { multipartAuthApi, jsonAuthApi } from "@/libs/axiosConfig";
import { Group } from "@/types/group";


interface Result {
  result: null;
  resultCode: number;
  resultMsg: string;
}



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
const enterGroup = async (groupCode: string): Promise<Result> => {
  return jsonAuthApi.post(`/groups/code`, { groupCode }).then((res) => res.data);
};

// 그룹 상세사항
const detailGroup = async (groupId: number) => {
  const {
    data: { result },
  } = await jsonAuthApi.get(`/groups/${groupId}`);
  return result;
};
export { createGroup, getGroupList, enterGroup, detailGroup };
