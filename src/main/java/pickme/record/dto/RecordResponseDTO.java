package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import pickme.record.model.Record;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "DTO for interview record responses")
public class RecordResponseDTO {

    private String userId;
    private List<EnterpriseRecordResponse> records;

    @Data
    public static class EnterpriseRecordResponse {
        private String enterpriseName;
        private String category;
        private Date createdAt;
        private List<RecordDetailResponse> details;
    }

    @Data
    public static class RecordDetailResponse {
        private String question;
        private String answer;
    }

}
