import { multipartAuthApi } from "@/libs/axiosConfig";
import { AgingEventResult } from "@/types/event";

// Aging 이벤트 후 picture 만들기
const agingEventApi = async (formData: FormData): Promise<AgingEventResult> => {
  const { data } = await multipartAuthApi.post("/pictures", formData);
  return data;
};

export { agingEventApi };
