package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO for RecordDetail responses")
public class RecordDetailResponseDTO {

    private String question;
    private String answer;

}
