package com.eumpyo.eum.config;

import com.eumpyo.eum.config.handler.JwtAccessDeniedHandler;
import com.eumpyo.eum.config.handler.JwtAuthenticationEntryPoint;
import com.eumpyo.eum.config.oauth2.CustomOAuth2UserService;
import com.eumpyo.eum.config.oauth2.filter.JwtAuthenticationFilter;
import com.eumpyo.eum.config.oauth2.filter.JwtExceptionFilter;
import com.eumpyo.eum.config.oauth2.handler.Oauth2AuthenticationFailureHandler;
import com.eumpyo.eum.config.oauth2.handler.Oauth2AuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
// 스프링 시큐리티를 사용하여 보안 구성을 정의
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    @Autowired
    private Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORS를 활성화하면, 다른 도메인에서의 리소스 요청을 처리할 수 있도록 서버의 응답 헤더에 Access-Control-Allow-Origin 헤더를 추가합니다.
        http.cors()
            // 이전 설정 메서드의 반환값인 HttpSecurity 객체를 반환하여, 다음 설정 메서드에서 이전 설정을 참조하도록 합니다.
            .and()
            // 이 기능은 CSRF 공격을 방지하기 위해 CSRF 토큰을 사용하여 요청을 검증합니다.
            .csrf().disable()
            // 권한 설정
            .authorizeRequests()
            .antMatchers("/api/v1/oauth2/**", "/home").permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint())
            .accessDeniedHandler(jwtAccessDeniedHandler())
            .and()
            // Spring Security JWT Filter Load
            .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)
            // JwtExceptionFilter
            .addFilterBefore(jwtExceptionFilter(), JwtAuthenticationFilter.class)
            // session stateless로 설정하여 Rest API에 적합한 상태 없는 인증을 적용합니다.
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
        // .form 기반의 로그인에 대해 비 활성화하며 커스텀으로 구성한 필터를 사용한다.
            .formLogin().disable()
        // authorizeRequests() 메서드는 antMatchers() 메서드와 함께 사용하여, 다양한 URL 경로나 리소스에 대한 접근 권한을 설정할 수 있습니다.
//            .authorizeRequests()
            .oauth2Login()				// OAuth2기반의 로그인인 경우
                // 인가 엔드포인트를 설정
                .authorizationEndpoint()
                .baseUri("/api/v1/oauth2/authorize")
                .and()
                // 인가 후 redirect_uri 엔드 포인트 설정, 등록한 redirect_uri로 설정해야 함
                .redirectionEndpoint()
                .baseUri("/api/v1/oauth2/callback/*")
                .and()
                .userInfoEndpoint()			// 로그인 성공 후 사용자정보를 가져온다
                .userService(customOAuth2UserService)	//사용자정보를 처리할 때 사용한다
                .and()
                .successHandler(oauth2AuthenticationSuccessHandler)
                .failureHandler(oauth2AuthenticationFailureHandler);

        return http.build();
    }

    /**
     * JWT 토큰을 통하여서 사용자를 인증합니다.
     *
     * @return JwtAuthorizationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthorizationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * JwtAuthenticationFilter에서 발생하는 exception를 처리합니다.
     *
     * @return JwtExceptionFilter
     */
    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(new ObjectMapper());
    }

    /**
     * authorizeRequests에서 유효한 자격증명을 제공하지 않고 접근하려
     *
     * @return 401 Exception
     */
    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    /**
     * authorizeRequests에서 필요한 권한이 없이 접근하려 할때 발생
     *
     * @return 403 Exception
     */
    @Bean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }
}
