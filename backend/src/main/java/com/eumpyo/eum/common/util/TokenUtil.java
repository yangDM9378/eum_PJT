package com.eumpyo.eum.common.util;

import com.eumpyo.eum.db.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;

@Service
@Slf4j
public class TokenUtil {
    @Value("${accessToken.TOKEN_VALIDATION_SECOND}")
    private int TOKEN_VALIDATION_SECOND;
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 24 * 2;

    final static public String ACCESS_TOKEN_NAME = "access";
    final static public String REFRESH_TOKEN_NAME = "refresh";
    @Value("${spring.jwt.accessSecret}")
    private String ACCESS_TOKEN_SECRET_KEY;
    @Value("${spring.jwt.refreshSecret}")
    private String REFRESH_TOKEN_SECRET_KEY;

    public String generateJwtToken(User user, String type) {
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

    private Map<String, Object> createClaims(User user) {
        // 공개 클레임에 사용자의 이름과 이메일을 설정하여 정보를 조회할 수 있다.
        Map<String, Object> claims = new HashMap<>();
        log.info("userId :" + user.getUserId());
        log.info("userEmail :" + user.getEmail());
        log.info("userName :" + user.getName());
        log.info("userBirthYear :" + user.getBirthYear());
        log.info("userGender :" + user.getGender());

        claims.put("userId", user.getUserId());
        claims.put("userEmail", user.getEmail());
        claims.put("userName", user.getName());
        claims.put("userBirthYear", user.getBirthYear());
        claims.put("userGender", user.getGender());

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
            return new Date(System.currentTimeMillis() + TOKEN_VALIDATION_SECOND * 1000L);
        } else { // // REFRESH_TOKEN_NAME
            return new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDATION_SECOND);
        }
    }

    /**
     * 토큰 정보를 기반으로 Claims 정보를 반환받는 메서드
     *
     * @param token : 토큰
     * @return Claims : Claims
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(ACCESS_TOKEN_SECRET_KEY)).build()
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.debug("클레임을 가져오는 중 에러 발생");

            e.printStackTrace();

            return null;
        }
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(ACCESS_TOKEN_SECRET_KEY).build().parseClaimsJws(token).getBody();
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            log.error(e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            log.error(e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            log.error(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            log.error(e.getMessage());
        }
        return false;
    }

    // 토큰을 받아 클레임을 만들고 권한정보를 빼서 시큐리티 유저객체를 만들어 Authentication 객체 반환
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        User user = User
                .builder()
                .userId(((Integer) claims.get("userId")).longValue())
                .name((String) claims.get("userName"))
                .birthYear((Integer) claims.get("userBirthYear"))
                .email((String) claims.get("userEmail"))
                .gender((Integer) claims.get("userGender"))
                .build();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }
}
