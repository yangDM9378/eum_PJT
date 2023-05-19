package com.eumpyo.eum.config.oauth2.filter;

import com.eumpyo.eum.common.util.TokenUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, JwtException {

        String token = getAuthenticationToken(request);

        // OAuth2 로그인
        if (token == null) {
            filterChain.doFilter(request, response);
        // Token을 통한 권한 인증
        } else if (tokenUtil.validateToken(token)) {
            // 토큰에서 유저네임, 권한을 뽑아 스프링 시큐리티 유저를 만들어 Authentication 반환
            Authentication authentication = (Authentication) tokenUtil.getAuthentication(token);
            // 해당 스프링 시큐리티 유저를 시큐리티 건텍스트에 저장, 즉 디비를 거치지 않음
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            throw new JwtException("잘못된 토큰입니다.");
        }
    }

    private String getAuthenticationToken(HttpServletRequest request) throws JwtException {
        // Read the Authorization header, where the JWT Token should be
        String token = request.getHeader("Authorization");

        if( token != null ) {
            try {
                token = URLDecoder.decode(token, "UTF-8");
                String[] parts = token.split(" "); // Bearer ~encodedToken
                if( parts.length == 2 ) {
                    String encodedToken = parts[1];
                    return encodedToken;
                }

                return null;
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }

        return null;
    }
}
