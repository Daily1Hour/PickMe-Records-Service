package pickme.record.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    public String extractUserId(String token) throws Exception {
        // "Bearer" 접두어 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 토큰을 파싱하여 userId 추출
        Claims claims = Jwts.parserBuilder()
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

}
