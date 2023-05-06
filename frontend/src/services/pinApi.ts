import { jsonAuthApi, multipartAuthApi } from "@/libs/axiosConfig";
import { Pin, PindetailResult } from "@/types/pin";
import axios from "axios";

// 핀만들기 API
const createPin = async (formData: FormData) => {
  const { data } = await multipartAuthApi.post("/pins", formData);
  return data;
};

// 그룹의 핀 리스트 가져오기
const getPinList = async (groupId: number): Promise<Array<Pin>> => {
  const response = await jsonAuthApi.get(`/pins/group/${groupId}`);
  const pinList: Array<Pin> = response.data.result;
  return pinList;
};

// 핀 디테일 페이지 가져오기
const getPinDetail = async (pinId: number): Promise<PindetailResult> => {
  const { data } = await jsonAuthApi.get(`/pins/${pinId}`);
  return data;
};

export { createPin, getPinList, getPinDetail };
