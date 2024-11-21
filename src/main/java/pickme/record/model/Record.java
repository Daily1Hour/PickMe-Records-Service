package pickme.record.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "records")
@Data
@Schema(description = "면접 기록 데이터 모델")
public class Record {

    @Id
    @Schema(description = "Post ID (PK)", example = "60d5f483f8d4b91234567890")
    private ObjectId postId;

    @Schema(description = "User ID", example = "user")
    private String userId;

    @Schema(description = "Enterprise Name", example = "Day Company")
    private String enterpriseName;

    @Schema(description = "Category", example = "1st interview")
    private String category;

    @Schema(description = "Question", example = "1분 자기소개")
    private String question;

    @Schema(description = "Answer", example = "안녕하십니까. 뫄뫄 기업 솨솨 직무에 지원한 김땡땡입니다.")
    private String answer;

    @Schema(description = "Creation Time", example = "2023-01-01T12:34:56Z")
    private Date createdAt;

}
