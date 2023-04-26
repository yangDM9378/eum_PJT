package com.eumpyo.eum.config.oauth2.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        try{
            filterChain.doFilter(request, response);
        } catch (JwtException e){
            Map<String, Object> map = new HashMap<>();

            map.put("status", 403);
            map.put("code", "JwtExceptionFilter 코드");
            map.put("message", "JwtExceptionFilter 메세지");

            log.error("JwtExceptionFilter 잘못된 토큰 발생");
            response.getWriter().write(objectMapper.writeValueAsString(map));
        }
    }
}
