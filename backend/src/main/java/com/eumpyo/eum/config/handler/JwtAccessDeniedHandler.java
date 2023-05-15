package com.eumpyo.eum.config.handler;

import com.eumpyo.eum.common.code.ErrorCode;
import com.eumpyo.eum.common.code.SuccessCode;
import com.eumpyo.eum.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("필요한 권한이 없습니다.");

        // HttpServletResponse에 ResponseEntity의 정보를 설정합니다.
        response.setStatus(ErrorCode.FORBIDDEN_ERROR.getStatus());
//        response.setContentType("application/json");
//        response.getWriter().write(ErrorCode.FORBIDDEN_ERROR.getMessage());
        response.getWriter().flush();
        response.getWriter().close();
    }
}