package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO for creating a new interview record")
public class RecordCreateDTO {

    @Schema(description = "User ID", example = "user123")
    @NotBlank(message = "User ID is required")
    private String userId;

    @Schema(description = "Enterprise Name", example = "Day Company")
    @NotBlank(message = "Enterprise Name is required")
    private String enterpriseName;

    @Schema(description = "Category", example = "1st interview")
    @NotBlank(message = "Category is required")
    private String category;

    @Schema(description = "Question", example = "1분 자기소개")
    @NotBlank(message = "Question is required")
    private String question;

    @Schema(description = "Answer", example = "안녕하십니까. 뫄뫄 기업 솨솨 직무에 지원한 김땡땡입니다.")
    @NotBlank(message = "Answer is required")
    private String answer;

}
