import { jsonAuthApi } from "@/libs/axiosConfig";
import { Picture, PictureDetail } from "@/types/picture";

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

// 핀 갤러리 이미지 상세 불러오기
const getPinImage = async (pictureId: number): Promise<PictureDetail> => {
  const response = await jsonAuthApi.get(`pictures/${pictureId}`);
  const data: PictureDetail = response.data;
  return data;
};

export { getGroupGallery, getpinImages, getPinImage };
