import axios from "axios";

// 핀만들기 API
const createPin = async (formData: FormData) => {
  const { data } = await axios.post("/api/v1/pins", formData);
  return data;
};

export { createPin };
