package pickme.record.config;

import java.util.List;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuration: Spring 설정 파일임을 명시
@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {
        // JWT를 사용할 수 있도록 Swagger 보안 설정 구성
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP) // 보안 방식으로 HTTP 사용
                .scheme("bearer") // JWT 토큰 인증 방식인 "bearer" 방식 사용
                .bearerFormat("JWT") // JWT 형식 사용
                .in(SecurityScheme.In.HEADER) // JWT 토큰을 HTTP 헤더에 포함시키도록 설정
                .name("Authorization"); // Authorization 헤더를 사용하도록 이름 지정

        // 보안 요구 사항 설정: Swagger UI가 Authorization 헤더를 포함한 JWT 인증 요구
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        // OpenAPI 설정 반환
        return new OpenAPI()
                .info(new Info().title("PickMe-Record").version("3.0.0")) // API 제목 및 버전 설정
                .addSecurityItem(securityRequirement) // 보안 요구 사항 추가
                .schemaRequirement("BearerAuth", securityScheme); // 보안 요구 사항에 대한 설명 추가
    }

    @Bean
    OpenApiCustomizer globalHeaderCustomizer() {
        // 커스텀 헤더 생성: 모든 API에 공통으로 사용
        // API Gateway에서 claim을 통해 사용자 정보를 전달
        List<Parameter> globalHeaders = List.of(
                createHeader("X-User-Id", "사용자 ID (식별값)", true, "74488dfc-e0f1-70ba-a329-2c0fdb8a54d6"),
                createHeader("X-User-Name", "사용자 이름", false, null),
                createHeader("X-Client-Id", "클라이언트 ID", false, null));

        // 모든 API operation에 헤더 추가
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().stream()
                .filter(operation -> !operation.getTags().contains("HealthCheck")) // HealthCheck API 제외
                .forEach(operation -> globalHeaders.forEach(operation::addParametersItem)));
    }

    private Parameter createHeader(String name, String description, boolean required, String defaultValue) {
        return new Parameter()
                .in("header")
                .name(name)
                .description(description)
                .required(required)
                .schema(new StringSchema()._default(defaultValue));
    }
}
