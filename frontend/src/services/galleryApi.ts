import { jsonAuthApi } from "@/libs/axiosConfig";
import { Picture } from "@/types/picture";

const getGroupGallery = async (group_id: number) => {
  const {
    data: { result },
  } = await jsonAuthApi.get(`/pictures/group/${group_id}`);
  return result;
};

// 핀 갤러리 이미지들 불러오기
const getpinImages = async (messageId: number): Promise<Picture[]> => {
  const response = await jsonAuthApi.get(`pictures/pin/${messageId}`);
  const data: Picture[] = response.data.result;
  return data;
};

export { getGroupGallery, getpinImages };
