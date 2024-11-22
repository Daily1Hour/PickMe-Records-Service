package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO for updating an existing RecordDetail")
public class RecordDetailUpdateDTO {

    @Schema(description = "Updated Question", example = "Updated question")
    @NotBlank(message = "Question is required")
    private String question;

    @Schema(description = "Updated Answer", example = "Updated answer")
    @NotBlank(message = "Answer is required")
    private String answer;

}
