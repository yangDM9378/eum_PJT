import { multipartAuthApi } from "@/libs/axiosConfig";
import { AgingEventResult, PoseEventResult } from "@/types/event";
import axios from "axios";

// Aging 이벤트 후 picture 만들기
const agingEventApi = async (formData: FormData): Promise<AgingEventResult> => {
  const { data } = await multipartAuthApi.post("/pictures", formData);
  return data;
};

//pose 이벤트
const fastApiUrl = process.env.NEXT_PUBLIC_FASTAPI_BASE_URL_DEFAULT || "";
const postPose = async (formdata: FormData): Promise<PoseEventResult> => {
  const result = await axios.post(fastApiUrl + "pose", formdata, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  const poseResult: PoseEventResult = result.data;
  console.log(poseResult);
  return poseResult;
};

export { agingEventApi, postPose };
