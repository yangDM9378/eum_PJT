import { jsonAuthApi } from "@/libs/axiosConfig";

// 유저 정보 가져오기
const getUser = async () => {
  const {
    data: { result },
  } = await jsonAuthApi.get(`/users`);
  return result;
};

export { getUser };
