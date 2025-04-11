package pickme.record.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.stereotype.Service;

/**
 * JWTService 클래스는 JWT 토큰을 처리하는 기능을 제공합니다.
 * 
 * <p>
 * 이 클래스는 JWT 토큰에서 특정 클레임 값을 추출하거나 토큰을 디코딩하는 등의 작업을 수행합니다.
 * </p>
 */
@Service
public class JWTService {
    /**
     * 주어진 JWT 토큰에서 "client_id" 클레임 값을 추출합니다.
     *
     * @param token "Bearer " 접두어가 포함된 JWT 토큰 문자열
     * @return "client_id" 클레임의 값 (문자열)
     * @throws Exception 토큰 디코딩 중 오류가 발생한 경우
     */
    public String extractToken(String token) throws Exception {
        // "Bearer" 접두어 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // JWT 토큰 디코딩
        DecodedJWT decodedJWT = JWT.decode(token);

        // "client_id" 클레임을 문자열로 추출하여 반환
        return decodedJWT.getClaim("client_id").asString();
    }

}
