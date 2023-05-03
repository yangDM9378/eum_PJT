import axios, { AxiosInstance } from "axios";

const BASE_URL = "https://i-eum-u.com/api/v1";
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
  axiosInstance.interceptors.response.use(
    async (response) => {
      // 엑세스 토큰 만료
      if (response.data.code === "G006") {
        const refreshtoken = localStorage.getItem("refreshtoken");
        // 리프레쉬 토큰이 있으면 리프레시 토큰으로 엑세스 토큰 재발급
        if (refreshtoken) {
          try {
            const reissueResponse = await axios.post("/auth/reissue", {
              refreshToken: refreshtoken,
            });
            // 토큰 재발급 실패시 리프레시도 만료되었다고 판단 되어 로그인 쪽으로 리다이렉트
            if (reissueResponse.data.code === "G999") {
              localStorage.removeItem("accesstoken");
              localStorage.removeItem("refreshtoken");
              window.location.href = "/";
            } else {
              // 재발급 되면 accesstoken과 refreshtoken를 localstorage에 다시 넣기
              console.log("리프레시로 엑세스토큰 재발급 완료");
              localStorage.setItem(
                "accesstoken",
                reissueResponse.data.result.accessToken
              );
              localStorage.setItem(
                "refreshtoken",
                reissueResponse.data.result.refreshToken
              );
              // 원래 요청 다시 시도
              return axiosInstance(response.config);
            }
          } catch (error) {
            // 토큰 재발급 실패
            localStorage.removeItem("accesstoken");
            localStorage.removeItem("refreshtoken");
            window.location.href = "/";
          }
        }
      }
      return response;
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
