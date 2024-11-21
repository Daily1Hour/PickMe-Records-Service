package pickme.record.service;

import pickme.record.dto.RecordCreateDTO;
import pickme.record.dto.RecordResponseDTO;
import pickme.record.dto.RecordUpdateDTO;

public interface RecordService {

    RecordResponseDTO createRecord(String userId, RecordCreateDTO recordCreateDTO);

    RecordResponseDTO getAllRecords(String userId);

    RecordResponseDTO.EnterpriseRecordResponse getRecord(String userId, String enterpriseName, String category);

    RecordResponseDTO.EnterpriseRecordResponse updateRecord(String userId, String enterpriseName, String category, RecordUpdateDTO updatedRecordDTO);

    boolean deleteRecord(String userId, String enterpriseName, String category);

}
