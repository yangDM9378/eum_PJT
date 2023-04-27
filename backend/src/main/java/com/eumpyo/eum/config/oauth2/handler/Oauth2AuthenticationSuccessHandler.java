package com.eumpyo.eum.config.oauth2.handler;

import com.eumpyo.eum.api.response.UserResponse;
import com.eumpyo.eum.common.util.TokenUtil;
import com.eumpyo.eum.config.oauth2.PrincipalDetails;
import com.eumpyo.eum.db.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    TokenUtil tokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        PrincipalDetails oAuth2User = (PrincipalDetails) authentication.getPrincipal();
        User user = oAuth2User.getUser();
        UserResponse userResponse = user.UserToDto();
        String token = tokenUtil.generateJwtToken(userResponse, "access");

        String targetUrl = UriComponentsBuilder
                .fromUriString("/home")
                .queryParam("accessToken","Bearer " + token)
                .build()
                .toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
