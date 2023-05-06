import axios from "axios";

export const removebgApi = async (imageUrl: string) => {
  const formData = new FormData();
  // base64를 blob형태로변환
  const blob = await (await fetch(imageUrl)).blob();
  formData.append("image", blob, "image.png");
  const response = await axios.post(
    "https://www.ailabapi.com/api/cutout/general/universal-background-removal",
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
        "ailabapi-api-key": process.env.NEXT_PUBLIC_AILAB_API_KEY,
      },
    }
  );
  return response;
};
