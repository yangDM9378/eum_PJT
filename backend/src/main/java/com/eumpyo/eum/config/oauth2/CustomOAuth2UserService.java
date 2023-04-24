package com.eumpyo.eum.config.oauth2;

import com.eumpyo.eum.config.oauth2.userInfo.AuthProvider;
import com.eumpyo.eum.config.oauth2.userInfo.KakaoUserInfo;
import com.eumpyo.eum.config.oauth2.userInfo.OAuth2UserInfo;
import com.eumpyo.eum.db.entity.User;
import com.eumpyo.eum.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals(AuthProvider.kakao.toString())){
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }
        log.info(oAuth2UserInfo.getName());
        log.info(oAuth2UserInfo.getEmail());
        log.info(oAuth2UserInfo.getGender());
        log.info(oAuth2UserInfo.getAgeRange());

        String name = oAuth2UserInfo.getName();

        String email = oAuth2UserInfo.getEmail();

        int gender = 1;
        if (oAuth2UserInfo.getGender().equals("male")) gender = 0;

        String [] age = oAuth2UserInfo.getAgeRange().split("~");

        LocalDate now = LocalDate.now();
        int birthYear = now.getYear() - Integer.parseInt(age[1]);

        Optional<User> byUsername = userRepository.findByEmail(email);
        User byUser = null;

        if(byUsername.isPresent()){
            byUser = byUsername.get();
        } else {
            log.info("회원가입 진행");

            byUser = User.builder()
                    .name(name)
                    .gender(gender)
                    .birthYear(birthYear)
                    .email(email)
                    .build();
            userRepository.save(byUser);
        }
        
        return new PrincipalDetails(byUser, oAuth2UserInfo);
    }
}
