package pickme.record.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HealthCheckController는 애플리케이션의 상태를 확인하기 위한 컨트롤러입니다.
 * 
 * <p>
 * 이 컨트롤러는 기본 경로("/")에 대한 GET 요청을 처리하며,
 * 애플리케이션이 정상적으로 작동 중임을 나타내는 간단한 "OK" 응답을 반환합니다.
 * </p>
 */
@RestController
@Tag(name = "HealthCheck")
public class HealthCheckController {
    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
