package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO for updating an existing interview record")
public class RecordUpdateDTO {

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
