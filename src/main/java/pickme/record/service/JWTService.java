package pickme.record.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    // JWT 토큰을 처리하는 로직
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
