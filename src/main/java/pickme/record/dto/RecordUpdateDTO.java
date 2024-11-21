package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO for updating an existing interview record")
public class RecordUpdateDTO {

    @Schema(description = "Record Details")
    private List<RecordDetailUpdateDTO> details;

    @Data
    public static class RecordDetailUpdateDTO {
        @Schema(description = "Question", example = "Updated question")
        @NotBlank(message = "Question is required")
        private String question;

        @Schema(description = "Answer", example = "Updated answer.")
        @NotBlank(message = "Answer is required")
        private String answer;
    }

}
