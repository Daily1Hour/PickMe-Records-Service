package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO for creating a new EnterpriseRecord")
public class InterviewRecordCreateDTO {

    @Schema(description = "Enterprise Name", example = "Day Company")
    @NotBlank(message = "Enterprise Name is required")
    private String enterpriseName;

}
