package pickme.record.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler 클래스는 애플리케이션 전역에서 발생하는 예외를 처리하기 위한 핸들러입니다.
 * 
 * <p>
 * 이 클래스는 {@code @RestControllerAdvice} 어노테이션을 사용하여 컨트롤러 전역에서 발생하는 예외를
 * 처리할 수 있도록 설정되어 있습니다.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * MethodArgumentNotValidException 예외를 처리하는 핸들러 메서드입니다.
     * 
     * @param validationException MethodArgumentNotValidException 예외 객체로,
     *                            유효성 검사 실패 시 발생합니다.
     * @return 유효성 검사 실패 필드와 메시지를 포함한 Map을 본문으로 가지는 ResponseEntity 객체를 반환하며,
     *         HTTP 상태 코드는 BAD_REQUEST(400)입니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException validationException) {
        Map<String, String> errors = new HashMap<>();

        validationException.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
