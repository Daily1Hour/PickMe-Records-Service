package pickme.record.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pickme.record.dto.RecordCreateDTO;
import pickme.record.dto.RecordResponseDTO;
import pickme.record.dto.RecordUpdateDTO;
import pickme.record.mapper.RecordMapper;
import pickme.record.model.Record;
import pickme.record.repository.RecordRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public RecordResponseDTO createRecord(String userId, RecordCreateDTO recordCreateDTO) {
        // Find the user's record
        Record record = recordRepository.findById(userId).orElseGet(() -> {
            Record newRecord = new Record();
            newRecord.setUserId(userId);
            newRecord.setRecords(new ArrayList<>());
            return newRecord;
        });

        // Check if the EnterpriseRecord already exists
        Record.EnterpriseRecord existingEnterpriseRecord = findEnterpriseRecord(record, recordCreateDTO.getEnterpriseName(), recordCreateDTO.getCategory());
        if (existingEnterpriseRecord != null) {
            throw new RuntimeException("EnterpriseRecord already exists");
        }

        // Create new EnterpriseRecord
        Record.EnterpriseRecord enterpriseRecord = new Record.EnterpriseRecord();
        enterpriseRecord.setEnterpriseName(recordCreateDTO.getEnterpriseName());
        enterpriseRecord.setCategory(recordCreateDTO.getCategory());
        enterpriseRecord.setCreatedAt(new Date());
        enterpriseRecord.setDetails(new ArrayList<>());

        // Add initial RecordDetail
        Record.RecordDetail detail = new Record.RecordDetail();
        detail.setQuestion(recordCreateDTO.getQuestion());
        detail.setAnswer(recordCreateDTO.getAnswer());
        enterpriseRecord.getDetails().add(detail);

        // Add the EnterpriseRecord to the user's records
        record.getRecords().add(enterpriseRecord);

        // Save the record
        recordRepository.save(record);

        // Map to response DTO
        return recordMapper.toResponseDTO(record);
    }

    @Override
    public RecordResponseDTO getAllRecords(String userId) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            return recordMapper.toResponseDTO(optionalRecord.get());
        } else {
            // Return empty response if no records found
            return new RecordResponseDTO();
        }
    }

    @Override
    public RecordResponseDTO.EnterpriseRecordResponse getRecord(String userId, String enterpriseName, String category) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record.EnterpriseRecord enterpriseRecord = findEnterpriseRecord(optionalRecord.get(), enterpriseName, category);
            if (enterpriseRecord != null) {
                return recordMapper.toEnterpriseRecordResponse(enterpriseRecord);
            }
        }
        return null;
    }

    @Override
    public RecordResponseDTO.EnterpriseRecordResponse updateRecord(String userId, String enterpriseName, String category, RecordUpdateDTO updatedRecordDTO) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.EnterpriseRecord enterpriseRecord = findEnterpriseRecord(record, enterpriseName, category);
            if (enterpriseRecord != null) {
                // Update details (question-answer pairs)
                enterpriseRecord.setDetails(new ArrayList<>());
                for (RecordUpdateDTO.RecordDetailUpdateDTO detailDTO : updatedRecordDTO.getDetails()) {
                    Record.RecordDetail detail = new Record.RecordDetail();
                    detail.setQuestion(detailDTO.getQuestion());
                    detail.setAnswer(detailDTO.getAnswer());
                    enterpriseRecord.getDetails().add(detail);
                }

                // Save the record
                recordRepository.save(record);

                // Map to response DTO
                return recordMapper.toEnterpriseRecordResponse(enterpriseRecord);
            }
        }
        return null;
    }

    @Override
    public boolean deleteRecord(String userId, String enterpriseName, String category) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.EnterpriseRecord enterpriseRecord = findEnterpriseRecord(record, enterpriseName, category);
            if (enterpriseRecord != null) {
                record.getRecords().remove(enterpriseRecord);
                recordRepository.save(record);
                return true;
            }
        }
        return false;
    }

    private Record.EnterpriseRecord findEnterpriseRecord(Record record, String enterpriseName, String category) {
        return record.getRecords().stream()
                .filter(er -> er.getEnterpriseName().equals(enterpriseName) && er.getCategory().equals(category))
                .findFirst()
                .orElse(null);
    }

}
