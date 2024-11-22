package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO for EnterpriseRecord sidebar data")
public class InterviewRecordSidebarDTO {

    private String enterpriseName;
    private String category;
    private Date createdAt;

}
