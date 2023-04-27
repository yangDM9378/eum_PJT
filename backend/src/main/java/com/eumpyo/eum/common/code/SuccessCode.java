package com.eumpyo.eum.common.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리를 한다.
 * Success CodeList : 성공 코드를 관리한다.
 * Success Code Constructor: 성공 코드를 사용하기 위한 생성자를 구성한다.
 *
 * @author choe
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessCode {

    /**
     * ******************************* Success CodeList ***************************************
     */
    // 삽입 성공 코드 (HTTP Response: 201 Created)
    INSERT(201, "Created", "INSERT SUCCESS"),
    // 조회 성공 코드 (HTTP Response: 200 OK)
    SELECT(200, "OK", "SELECT SUCCESS"),
    // 수정 성공 코드 (HTTP Response: 201 Created)
    UPDATE(200, "OK", "UPDATE SUCCESS"),
    // 삭제 성공 코드 (HTTP Response: 200 OK)
    DELETE(200, "OK", "DELETE SUCCESS"),

    ; // End

    /**
     * ******************************* Success Code Constructor ***************************************
     */
    // 성공 코드의 '코드 상태'를 반환한다.
    private int status;

    // 성공 코드의 '코드 값'을 반환한다.
    private String code;

    // 성공 코드의 '코드 메시지'를 반환한다.s
    private String message;

    // 생성자 구성
    SuccessCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

