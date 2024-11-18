package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO for updating an existing interview record")
public class RecordUpdateDto {

    @Schema(description = "Category", example = "1st interview")
    @NotBlank(message = "Category is required")
    private String category;

    @Schema(description = "Content", example = "(1분 자기소개) 안녕하십니까. 저는...")
    @NotBlank(message = "Content is required")
    private String content;

}
