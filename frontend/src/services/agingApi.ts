import axios from "axios";

const agingApi = async (imageUrl: string) => {
  try {
    const oldFormData = new FormData();
    const kidFormData = new FormData();
    const blob = await (await fetch(imageUrl)).blob();
    oldFormData.append("image", blob, "oldimage.png");
    oldFormData.append("action_type", "V2_AGE");
    oldFormData.append("target", "55");
    kidFormData.append("image", blob, "kidimage.png");
    kidFormData.append("action_type", "V2_AGE");
    kidFormData.append("target", "15");

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
  } catch (error) {
    return { oldImage: null, kidImage: null };
  }
};

export default agingApi;
