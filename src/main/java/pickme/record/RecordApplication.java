package pickme.record;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RecordApplication 클래스는 PickMe-Record-Service 애플리케이션의 진입점(entry point)입니다.
 * 
 * <p>
 * 이 클래스는 Spring Boot 애플리케이션으로 설정되며,
 * {@code SpringApplication.run} 메서드를 호출하여 애플리케이션을 시작합니다.
 * </p>
 * 
 * <p>
 * 주요 기능:
 * </p>
 * <ul>
 * <li>Spring Boot 애플리케이션 초기화 및 실행</li>
 * </ul>
 */
@SpringBootApplication
public class RecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecordApplication.class, args);
    }

}
