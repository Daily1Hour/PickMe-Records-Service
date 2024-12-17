package pickme.record.mapper;

import org.mapstruct.Mapper;
import pickme.record.dto.InterviewRecordResponseDTO;
import pickme.record.dto.InterviewRecordSidebarDTO;
import pickme.record.dto.RecordDetailResponseDTO;
import pickme.record.model.Record;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    // InterviewRecord를 InterviewRecordResponseDTO로 Mapping
    default InterviewRecordResponseDTO toInterviewRecordResponse(Record.InterviewRecord interviewRecord) {
        if (interviewRecord == null) {
            return null;
        }
        InterviewRecordResponseDTO dto = new InterviewRecordResponseDTO();
        dto.setInterviewRecordId(interviewRecord.getInterviewRecordId());
        dto.setEnterpriseName(interviewRecord.getEnterpriseName());
        dto.setCategory(interviewRecord.getCategory());
        dto.setCreatedAt(interviewRecord.getCreatedAt());
        dto.setUpdatedAt(interviewRecord.getUpdatedAt());
        dto.setDetails(toRecordDetailResponseList(interviewRecord.getDetails()));
        return dto;
    }

    // InterviewRecord를 InterviewRecordSidebarDTO로 Mapping
    default InterviewRecordSidebarDTO toInterviewRecordSidebarDTO(Record.InterviewRecord interviewRecord) {
        if (interviewRecord == null) {
            return null;
        }
        InterviewRecordSidebarDTO dto = new InterviewRecordSidebarDTO();
        dto.setInterviewRecordId(interviewRecord.getInterviewRecordId());
        dto.setEnterpriseName(interviewRecord.getEnterpriseName());
        dto.setCategory(interviewRecord.getCategory());
        dto.setCreatedAt(interviewRecord.getCreatedAt());
        dto.setUpdatedAt(interviewRecord.getUpdatedAt());
        return dto;
    }

    // RecordDetail을 RecordDetailResponseDTO로 Mapping
    default RecordDetailResponseDTO toRecordDetailResponse(Record.RecordDetail detail) {
        if (detail == null) {
            return null;
        }
        RecordDetailResponseDTO dto = new RecordDetailResponseDTO();
        dto.setQuestion(detail.getQuestion());
        dto.setAnswer(detail.getAnswer());
        return dto;
    }

    // List<RecordDetail>을 List<RecordDetailResponseDTO>로 Mapping
    default List<RecordDetailResponseDTO> toRecordDetailResponseList(List<Record.RecordDetail> details) {
        if (details == null) {
            return null;
        }
        return details.stream()
                .map(this::toRecordDetailResponse)
                .collect(Collectors.toList());
    }

}
