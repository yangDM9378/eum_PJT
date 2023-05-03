import axios from "axios";

const agingApi = async (imageUrl: string) => {
  const oldFormData = new FormData();
  const kidFormData = new FormData();
  const blob = await (await fetch(imageUrl)).blob();
  oldFormData.append("image", blob, "oldimage.png");
  oldFormData.append("action_type", "TO_OLD");
  kidFormData.append("image", blob, "kidimage.png");
  kidFormData.append("action_type", "TO_KID");

  const response = await axios.all([
    axios.post(
      "https://www.ailabapi.com/api/portrait/effects/face-attribute-editing",
      oldFormData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
          "ailabapi-api-key": process.env.NEXT_PUBLIC_AILAB_API_KEY,
        },
      }
    ),
    axios.post(
      "https://www.ailabapi.com/api/portrait/effects/face-attribute-editing",
      kidFormData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
          "ailabapi-api-key": process.env.NEXT_PUBLIC_AILAB_API_KEY,
        },
      }
    ),
  ]);
  const oldImage = response[0].data.result.image;
  const kidImage = response[1].data.result.image;
  return { oldImage, kidImage };
};

export default agingApi;
