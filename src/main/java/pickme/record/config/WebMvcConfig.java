package pickme.record.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import pickme.record.config.security.JWTInterceptor;

/**
 * WebMvcConfig 클래스는 Spring MVC 설정을 담당하는 구성 클래스입니다.
 * 
 * <p>
 * 이 클래스는 {@link WebMvcConfigurer}를 구현하여 CORS 설정 및 인터셉터 등록을 처리합니다.
 * </p>
 * 
 * <ul>
 * <li><b>CORS 설정:</b> {@code addCorsMappings} 메서드를 통해 모든 경로에 대해 모든 출처와
 * HTTP 메서드를 허용합니다.</li>
 * <li><b>인터셉터 등록:</b> {@code addInterceptors} 메서드를 통해 {@link JWTInterceptor}를
 * "/record/**" 경로에 적용하며,
 * "/record/swagger-ui/**" 경로는 제외합니다.</li>
 * </ul>
 * 
 * <p>
 * 이 클래스는 {@code @Configuration} 어노테이션을 사용하여 Spring 컨텍스트에 등록되며,
 * {@code @RequiredArgsConstructor}를 통해 생성자 주입을 지원합니다.
 * </p>
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JWTInterceptor jwtInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/record/**")
                .excludePathPatterns("/record/swagger-ui/**");
    }
}
