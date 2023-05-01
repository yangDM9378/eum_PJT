package com.eumpyo.eum.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
        //필요한 권한이 없이 접근하려 할때 403
        Map<String, Object> map = new HashMap<>();

        map.put("status", 403);
        map.put("code", "FORBIDDEN 코드");
        map.put("message", "필요한 권한이 없습니다.");

        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}