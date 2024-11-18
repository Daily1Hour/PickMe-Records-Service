package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO for creating a new interview record")
public class RecordCreateDto {

    @Schema(description = "User ID", example = "user123")
    @NotBlank(message = "User ID is required")
    private String userId;

    @Schema(description = "Category", example = "1st interview")
    @NotBlank(message = "Category is required")
    private String category;

    @Schema(description = "Content", example = "(1분 자기소개) 안녕하십니까. 저는...")
    @NotBlank(message = "Content is required")
    private String content;

}
