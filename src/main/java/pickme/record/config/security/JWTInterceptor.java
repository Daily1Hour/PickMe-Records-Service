package pickme.record.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * HttpServletRequest request:
         * - 클라이언트 요청 정보를 담고 있는 객체
         * - 헤더(Header), 요청 파라미터(Parameter), URI 등의 데이터를 읽을 수 있음
         */

        /*
         * HttpServletResponse response:
         * - 서버가 클라이언트에게 응답을 보낼 때 사용하는 객체
         * - 응답 상태 코드, 헤더(Header), 본문(Body) 등을 설정할 수 있음
         */

        /*
         * Object handler:
         * - 현재 요청을 처리할 핸들러(컨트롤러 메서드)에 대한 정보
         * - 주로 디버깅이나 특정 조건에서 핸들러를 식별할 때 사용
         * 모든 요청에 대해 인터셉터를 적용할 경우, handler는 사용하지 않아도 됩니다
         */

        // OPTIONS 요청은 인증 처리 생략
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; 
        }

        // 클라이언트 요청 헤더에서 Authorization 정보를 가져옴
        // 인가 용도로 필요하면 엑세스 토큰 사용
        String accessToken = request.getHeader("Authorization");

        // Api Gateway에서 JWT 토큰을 검증한 후, 사용자 정보를 헤더에 추가하여 전달
        String userId = request.getHeader("X-User-Id");
        String userName = request.getHeader("X-Username");
        String userSub = request.getHeader("X-User-Sub");

        // HttpServletRequest에 사용자 정보를 속성으로 추가하여 컨트롤러에서 사용할 수 있게 함
        request.setAttribute("userId", userId);
        request.setAttribute("userName", userName);
        request.setAttribute("userSub", userSub);

        // 요청 처리를 계속 진행
        return true;
    }

}
