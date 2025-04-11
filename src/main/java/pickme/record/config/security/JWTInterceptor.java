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
    /**
     * 컨트롤러에 도달하기 전에 HTTP 요청을 가로채서 전처리를 수행합니다.
     *
     * @param request  클라이언트 요청 정보를 담고 있는 HttpServletRequest 객체로,
     *                 헤더, 파라미터, URI 등의 데이터를 포함합니다.
     * @param response 클라이언트에게 응답을 보내는 데 사용되는 HttpServletResponse 객체로,
     *                 상태 코드, 헤더, 본문 내용을 설정할 수 있습니다.
     * @param handler  현재 요청을 처리할 핸들러(컨트롤러 메서드)에 대한 정보로,
     *                 주로 디버깅이나 특정 조건에서 핸들러를 식별하는 데 사용됩니다.
     * @return {@code true}이면 요청 처리를 계속 진행하고, {@code false}이면 요청을 차단합니다.
     * @throws Exception 요청 전처리 중 오류가 발생한 경우 예외를 던집니다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // OPTIONS 요청은 인증 처리 생략
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 클라이언트 요청 헤더에서 Authorization 정보를 가져옴
        // 인가 용도로 필요하면 엑세스 토큰 사용
        // String accessToken = request.getHeader("Authorization");

        // API Gateway에서 JWT 토큰 검증 후 전달한 사용자 관련 정보를 커스텀 헤더에서 추출
        String userId = request.getHeader("X-User-Id"); // 사용자 ID (식별값)
        String userName = request.getHeader("X-User-Name"); // 사용자 이름
        String clintId = request.getHeader("X-Client-Id"); // 클라이언트 앱 ID

        // HttpServletRequest에 사용자 정보를 속성으로 추가하여 컨트롤러에서 사용할 수 있게 함
        request.setAttribute("userId", userId);
        request.setAttribute("userName", userName);
        request.setAttribute("clintId", clintId);

        return true;
    }
}
