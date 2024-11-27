package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO for updating an existing EnterpriseRecord")
public class InterviewRecordUpdateDTO {

    @Schema(description = "New Enterprise Name", example = "Updated Company")
    @NotBlank(message = "Enterprise Name is required")
    private String enterpriseName;

    @Schema(description = "New Category", example = "Final interview")
    @NotBlank(message = "Category is required")
    private String category;

}
