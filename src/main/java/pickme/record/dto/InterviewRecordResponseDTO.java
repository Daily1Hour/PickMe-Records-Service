package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "DTO for EnterpriseRecord responses")
public class InterviewRecordResponseDTO {

    private String interviewRecordId;
    private String enterpriseName;
    private String category;
    private Date createdAt;
    private Date updatedAt;
    private List<RecordDetailResponseDTO> details;

}
