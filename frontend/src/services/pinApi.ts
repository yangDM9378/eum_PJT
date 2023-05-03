import axios from "axios";
import { jsonAuthApi } from "@/libs/axiosConfig";

// 핀만들기 API
const createPin = async (formData: FormData) => {
  const { data } = await axios.post("/api/v1/pins", formData);
  return data;
};

// 그룹의 핀 리스트 가져오기
const getPinList = async (groupId: number) => {
  const {
    data: { result },
  } = await jsonAuthApi.get(`/pins/group/${groupId}`);
  return result;
};

export { createPin, getPinList };
