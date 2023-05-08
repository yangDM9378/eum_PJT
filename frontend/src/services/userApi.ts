import { jsonAuthApi } from "@/libs/axiosConfig";
import { Usertype } from "@/types/user";

// 유저 정보 가져오기
const getUser = async (): Promise<Usertype> => {
  const response = await jsonAuthApi.get(`/users`);
  const userData: Usertype = response.data.result;
  return userData;
};

export { getUser };
