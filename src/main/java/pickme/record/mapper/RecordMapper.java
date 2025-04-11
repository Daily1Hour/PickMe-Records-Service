package pickme.record.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import pickme.record.dto.InterviewRecordResponseDTO;
import pickme.record.dto.InterviewRecordSidebarDTO;
import pickme.record.dto.RecordDetailResponseDTO;
import pickme.record.model.Record;

/**
 * RecordMapper는 Record 엔티티와 DTO 간의 변환 작업을 수행하는 매퍼 인터페이스입니다.
 *
 * 이 매퍼는 기본적으로 null 체크를 수행하며, null 입력값에 대해 null을 반환합니다.
 * 변환 작업은 각 필드 값을 매핑하여 새로운 DTO 객체를 생성하는 방식으로 이루어집니다.
 */
@Mapper(componentModel = "spring")
public interface RecordMapper {
    /**
     * 주어진 InterviewRecord 객체를 InterviewRecordResponseDTO 객체로 변환합니다.
     *
     * @param interviewRecord 변환할 InterviewRecord 객체
     * @return 변환된 InterviewRecordResponseDTO 객체
     */
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

    /**
     * 주어진 InterviewRecord 객체를 InterviewRecordSidebarDTO 객체로 변환합니다.
     *
     * @param interviewRecord 변환할 InterviewRecord 객체
     * @return 변환된 InterviewRecordSidebarDTO 객체
     */
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

    /**
     * 주어진 Record.RecordDetail 객체를 RecordDetailResponseDTO 객체로 변환합니다.
     * 
     * @param detail 변환할 Record.RecordDetail 객체
     * @return 변환된 RecordDetailResponseDTO 객체
     */
    default RecordDetailResponseDTO toRecordDetailResponse(Record.RecordDetail detail) {
        if (detail == null) {
            return null;
        }
        RecordDetailResponseDTO dto = new RecordDetailResponseDTO();
        dto.setQuestion(detail.getQuestion());
        dto.setAnswer(detail.getAnswer());
        return dto;
    }

    /**
     * 주어진 Record.RecordDetail 객체 리스트를 RecordDetailResponseDTO 객체 리스트로 변환합니다.
     *
     * @param details 변환할 Record.RecordDetail 객체 리스트
     * @return 변환된 RecordDetailResponseDTO 객체 리스트
     */
    default List<RecordDetailResponseDTO> toRecordDetailResponseList(List<Record.RecordDetail> details) {
        if (details == null) {
            return null;
        }
        return details.stream()
                .map(this::toRecordDetailResponse)
                .collect(Collectors.toList());
    }
}
