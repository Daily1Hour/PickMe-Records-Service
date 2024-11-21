package pickme.record.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "records")
@Data
@Schema(description = "면접 기록 데이터 모델")
public class Record {

    @Id
    private String userId;

    private List<EnterpriseRecord> records;

    @Data
    public static class EnterpriseRecord {
        private String enterpriseName;
        private String category;
        private Date createdAt;
        private List<RecordDetail> details;
    }

    @Data
    public static class RecordDetail {
        private String question;
        private String answer;
    }

}
