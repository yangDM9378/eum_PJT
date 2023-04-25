package com.eumpyo.eum.common.util;

import com.eumpyo.eum.api.response.UserResponse;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TokenUtil {
    public final static long TOKEN_VALIDATION_SECOND = 1000L * 60 * 300;
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 24 * 2;

    final static public String ACCESS_TOKEN_NAME = "access";
    final static public String REFRESH_TOKEN_NAME = "refresh";
    @Value("${spring.jwt.accessSecret}")
    private String ACCESS_TOKEN_SECRET_KEY;
    @Value("${spring.jwt.refreshSecret}")
    private String REFRESH_TOKEN_SECRET_KEY;

    public String generateJwtToken(UserResponse user, String type) {
        // 사용자 시퀀스를 기준으로 JWT 토큰을 발급하여 반환해줍니다.
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())                              // Header 구성
                .setClaims(createClaims(user))                       // Payload - Claims 구성
                .setSubject(String.valueOf(user.getEmail()))        // Payload - Subject 구성, JWT 토큰의 주체를 설정하는 메서드
                .signWith(SignatureAlgorithm.HS256, createSignature(type))  // Signature 구성
                .setExpiration(createExpiredDate(type));                    // Expired Date 구성
        return builder.compact();
    }

    /**
     * JWT의 "헤더" 값을 생성해주는 메서드
     *
     * @return HashMap<String, Object>
     */
    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    private Map<String, Object> createClaims(UserResponse user) {
        // 공개 클레임에 사용자의 이름과 이메일을 설정하여 정보를 조회할 수 있다.
        Map<String, Object> claims = new HashMap<>();
        log.info("userId :" + user.getEmail());
        claims.put("userId", user.getEmail());
        return claims;
    }

    /**
     * JWT "서명(Signature)" 발급을 해주는 메서드
     *
     * @return Key
     */
    private Key createSignature(String type) {
        if(type.equals(ACCESS_TOKEN_NAME)) {
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(ACCESS_TOKEN_SECRET_KEY);
            return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        } else {  // REFRESH_TOKEN_NAME
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(REFRESH_TOKEN_SECRET_KEY);
            return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        }
    }

    /**
     * 토큰의 만료기간을 지정하는 함수
     *
     * @return Calendar
     */
    private Date createExpiredDate(String type) {
        if (type.equals(ACCESS_TOKEN_NAME)) {
            return new Date(System.currentTimeMillis() + TOKEN_VALIDATION_SECOND);
        } else { // // REFRESH_TOKEN_NAME
            return new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDATION_SECOND);
        }
    }

}
