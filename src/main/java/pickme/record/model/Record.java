package pickme.record.model;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 면접 기록 데이터 모델을 나타내는 클래스입니다.
 */
@Document(collection = "records")
@Data
@Schema(description = "면접 기록 데이터 모델")
public class Record {

    /**
     * 사용자의 고유 ID를 나타냅니다.
     */
    @Id
    private String userId;

    /**
     * 사용자의 면접 기록 목록을 나타냅니다.
     */
    private List<InterviewRecord> records;

    /**
     * 개별 면접 기록을 나타내는 클래스입니다.
     */
    @Data
    public static class InterviewRecord {
        private String interviewRecordId;
        private String enterpriseName;
        private String category;
        private Date createdAt;
        private Date updatedAt;
        private List<RecordDetail> details;
    }

    /**
     * 면접 질문 및 답변 세부 정보를 나타내는 클래스입니다.
     */
    @Data
    public static class RecordDetail {
        private String question;
        private String answer;
    }
}
