import { jsonAuthApi } from "@/libs/axiosConfig";

const getGroupGallery = async (group_id: number) => {
  const {
    data: { result },
  } = await jsonAuthApi.get(`/pictures/group/${group_id}`);
  return result;
};

export { getGroupGallery };
