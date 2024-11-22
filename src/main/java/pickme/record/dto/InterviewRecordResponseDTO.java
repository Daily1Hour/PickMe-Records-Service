package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "DTO for EnterpriseRecord responses")
public class InterviewRecordResponseDTO {

    private String enterpriseName;
    private String category;
    private Date createdAt; // 필요할까?
    private List<RecordDetailResponseDTO> details;

}
