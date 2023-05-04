import axios, { AxiosResponse } from "axios";
type test = { message: string };

export async function getFastApiData(): Promise<null> {
  try {
    const response: AxiosResponse<test> = await axios.get(
      `http://localhost:8000/`
    );
    return response;
  } catch (error) {
    console.error(error);
    return null;
  }
}
