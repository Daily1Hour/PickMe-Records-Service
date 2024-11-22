package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO for creating a new RecordDetail")
public class RecordDetailCreateDTO {

    @Schema(description = "Question", example = "Tell me about yourself")
    @NotBlank(message = "Question is required")
    private String question;

    @Schema(description = "Answer", example = "I am a highly motivated individual...")
    @NotBlank(message = "Answer is required")
    private String answer;

}
