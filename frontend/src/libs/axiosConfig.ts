import axios, { AxiosInstance } from "axios";
import Cookies from "js-cookie";

const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL_DEFAULT;
axios.defaults.baseURL = BASE_URL;

const createAxiosInstance = (contentType: string): AxiosInstance => {
  return axios.create({
    baseURL: BASE_URL,
    headers: {
      "Content-Type": contentType,
    },
  });
};

const applyResponseInterceptor = (axiosInstance: AxiosInstance) => {
  axiosInstance.interceptors.request.use(
    (request) => {
      const accesstoken = Cookies.get("accessToken");
      request.headers.Authorization = accesstoken
        ? `Bearer ${accesstoken}`
        : null;
      return request;
    },
    (error) => {
      return Promise.reject(error);
    }
  );
};

const multipartAuthApi: AxiosInstance = createAxiosInstance(
  "multipart/form-data"
);
applyResponseInterceptor(multipartAuthApi);

const jsonAuthApi: AxiosInstance = createAxiosInstance("application/json");
applyResponseInterceptor(jsonAuthApi);

export { multipartAuthApi, jsonAuthApi };
