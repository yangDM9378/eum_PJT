package com.eumpyo.eum.config.handler;

import com.eumpyo.eum.common.code.ErrorCode;
import com.eumpyo.eum.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.info("유효한 자격증명을 제공하지 않고 있습니다.");
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401

        // HttpServletResponse에 ResponseEntity의 정보를 설정합니다.
        response.setStatus(ErrorCode.UNAUTHORIZED_ERROR.getStatus());
//        response.setContentType("application/json");
//        response.getWriter().write(ErrorCode.UNAUTHORIZED_ERROR.getMessage());
        response.getWriter().flush();
        response.getWriter().close();
    }
}