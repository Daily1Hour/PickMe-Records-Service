package pickme.record.mapper;

import org.mapstruct.Mapper;
import pickme.record.dto.RecordResponseDTO;
import pickme.record.model.Record;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    default RecordResponseDTO toResponseDTO(Record record) {
        if (record == null) {
            return null;
        }
        RecordResponseDTO dto = new RecordResponseDTO();
        dto.setUserId(record.getUserId());
        dto.setRecords(toEnterpriseRecordResponseList(record.getRecords()));
        return dto;
    }

    default RecordResponseDTO.EnterpriseRecordResponse toEnterpriseRecordResponse(Record.EnterpriseRecord enterpriseRecord) {
        if (enterpriseRecord == null) {
            return null;
        }
        RecordResponseDTO.EnterpriseRecordResponse dto = new RecordResponseDTO.EnterpriseRecordResponse();
        dto.setEnterpriseName(enterpriseRecord.getEnterpriseName());
        dto.setCategory(enterpriseRecord.getCategory());
        dto.setCreatedAt(enterpriseRecord.getCreatedAt());
        dto.setDetails(toRecordDetailResponseList(enterpriseRecord.getDetails()));
        return dto;
    }

    private List<RecordResponseDTO.EnterpriseRecordResponse> toEnterpriseRecordResponseList(List<Record.EnterpriseRecord> enterpriseRecords) {
        if (enterpriseRecords == null) {
            return null;
        }
        return enterpriseRecords.stream()
                .map(this::toEnterpriseRecordResponse)
                .collect(Collectors.toList());
    }

    private List<RecordResponseDTO.RecordDetailResponse> toRecordDetailResponseList(List<Record.RecordDetail> details) {
        if (details == null) {
            return null;
        }
        return details.stream()
                .map(this::toRecordDetailResponse)
                .collect(Collectors.toList());
    }

    private RecordResponseDTO.RecordDetailResponse toRecordDetailResponse(Record.RecordDetail detail) {
        if (detail == null) {
            return null;
        }
        RecordResponseDTO.RecordDetailResponse dto = new RecordResponseDTO.RecordDetailResponse();
        dto.setQuestion(detail.getQuestion());
        dto.setAnswer(detail.getAnswer());
        return dto;
    }

}
