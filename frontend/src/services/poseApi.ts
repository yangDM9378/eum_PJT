import { multipartAuthApi } from "@/libs/axiosConfig";
import axios, { AxiosResponse } from "axios";
type test = { message: string };

export async function getFastApiData(): Promise<test> {
  const { data }: AxiosResponse<test> = await axios.get(
    `http://localhost:8000/`
  );
  return data;
}
