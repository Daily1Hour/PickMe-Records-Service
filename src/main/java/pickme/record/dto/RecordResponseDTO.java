package pickme.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
@Schema(description = "DTO for interview record responses")
public class RecordResponseDTO {

    @Schema(description = "Post ID (PK)", example = "60d5f483f8d4b91234567890")
    private String postId;

    @Schema(description = "User ID", example = "user123")
    private String userId;

    @Schema(description = "Enterprise Name", example = "Day Company")
    private String enterpriseName;

    @Schema(description = "Category", example = "1st interview")
    private String category;

    @Schema(description = "Question", example = "1분 자기소개")
    private String question;

    @Schema(description = "Answer", example = "안녕하십니까. 뫄뫄 기업 솨솨 직무에 지원한 김땡땡입니다.")
    private String answer;

    @Schema(description = "Creation Time", example = "2024-01-01T12:34:56Z")
    private Date createdAt;

}
