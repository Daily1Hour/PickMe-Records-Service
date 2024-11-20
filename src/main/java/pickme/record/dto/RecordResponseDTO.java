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

    @Schema(description = "Category", example = "1st interview")
    private String category;

    @Schema(description = "Content", example = "(1분 자기소개) 안녕하십니까. 저는...")
    private String content;

    @Schema(description = "Creation Time", example = "2024-01-01T12:34:56Z")
    private Date createdAt;

}
