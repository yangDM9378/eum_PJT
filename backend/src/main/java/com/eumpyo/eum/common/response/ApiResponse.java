package com.eumpyo.eum.common.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {
    // API 응답 결과 Response
    private T result;

    // API 응답 코드 Response
    private int resultCode;

    // API 응답 코드 Message
    private String resultMsg;

    @Builder
    public ApiResponse(T result, int resultCode, String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
