package pickme.record.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pickme.record.dto.*;
import pickme.record.mapper.RecordMapper;
import pickme.record.model.Record;
import pickme.record.repository.RecordRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordMapper recordMapper;

    private static final Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);


    // InterviewRecord 관련 메서드

    @Override
    public InterviewRecordResponseDTO createInterviewRecord(String userId, InterviewRecordCreateDTO interviewRecordCreateDTO) {
        Record record = recordRepository.findById(userId).orElseGet(() -> {
            Record newRecord = new Record();
            newRecord.setUserId(userId);
            newRecord.setRecords(new ArrayList<>());
            return newRecord;
        });

        // 새로운 InterviewRecord 생성
        Record.InterviewRecord interviewRecord = new Record.InterviewRecord();
        interviewRecord.setInterviewRecordId(UUID.randomUUID().toString());
        interviewRecord.setEnterpriseName(interviewRecordCreateDTO.getEnterpriseName());
        interviewRecord.setCategory(interviewRecordCreateDTO.getCategory());
        Date now = new Date();
        interviewRecord.setCreatedAt(now); // 현재 시간 설정
        interviewRecord.setUpdatedAt(now); // 현재 시간 설정
        interviewRecord.setDetails(new ArrayList<>());

        // Record에 추가
        record.getRecords().add(interviewRecord);

        // 저장
        recordRepository.save(record);

        return recordMapper.toInterviewRecordResponse(interviewRecord);
    }

    @Override
    public InterviewRecordResponseDTO getInterviewRecordById(String userId, String interviewRecordId, int page, int size) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record.InterviewRecord interviewRecord = findInterviewRecordById(optionalRecord.get(), interviewRecordId);
            if (interviewRecord != null) {
                // RecordDetail에 페이징 적용
                List<Record.RecordDetail> allDetails = interviewRecord.getDetails();
                int start = page * size;
                int end = start + size;

                List<Record.RecordDetail> paginatedDetails;
                try {
                    paginatedDetails = allDetails.subList(start, Math.min(end, allDetails.size()));
                    InterviewRecordResponseDTO responseDTO = recordMapper.toInterviewRecordResponse(interviewRecord);
                    responseDTO.setDetails(recordMapper.toRecordDetailResponseList(paginatedDetails));
                    return responseDTO;
                } catch (IndexOutOfBoundsException e) {
                    logger.warn("Invalid pagination parameters: start={}, size={}, total={}", start, size, allDetails.size(), e);
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public InterviewRecordResponseDTO updateInterviewRecord(String userId, String interviewRecordId, InterviewRecordUpdateDTO interviewRecordUpdateDTO) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.InterviewRecord interviewRecord = findInterviewRecordById(record, interviewRecordId);
            if (interviewRecord != null) {
                interviewRecord.setEnterpriseName(interviewRecordUpdateDTO.getEnterpriseName());
                interviewRecord.setCategory(interviewRecordUpdateDTO.getCategory());
                interviewRecord.setUpdatedAt(new Date()); // updatedAt 갱신
                recordRepository.save(record);
                return recordMapper.toInterviewRecordResponse(interviewRecord);
            }
        }
        return null;
    }

    @Override
    public boolean deleteInterviewRecord(String userId, String interviewRecordId) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.InterviewRecord interviewRecord = findInterviewRecordById(record, interviewRecordId);
            if (interviewRecord != null) {
                record.getRecords().remove(interviewRecord);
                recordRepository.save(record);
                return true;
            }
        }
        return false;
    }

    // RecordDetail 관련 메서드

    @Override
    public RecordDetailResponseDTO createRecordDetail(String userId, String interviewRecordId, RecordDetailCreateDTO recordDetailCreateDTO) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.InterviewRecord interviewRecord = findInterviewRecordById(record, interviewRecordId);
            if (interviewRecord != null) {
                Record.RecordDetail newDetail = new Record.RecordDetail();
                newDetail.setQuestion(recordDetailCreateDTO.getQuestion());
                newDetail.setAnswer(recordDetailCreateDTO.getAnswer());
                interviewRecord.getDetails().add(newDetail);
                // updatedAt 갱신
                interviewRecord.setUpdatedAt(new Date());
                recordRepository.save(record);
                return recordMapper.toRecordDetailResponse(newDetail);
            }
        }
        return null;
    }

    @Override
    public RecordDetailResponseDTO updateRecordDetail(String userId, String interviewRecordId, int detailIndex, RecordDetailUpdateDTO recordDetailUpdateDTO) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.InterviewRecord interviewRecord = findInterviewRecordById(record, interviewRecordId);
            if (interviewRecord != null && isValidIndex(detailIndex, interviewRecord.getDetails().size())) {
                Record.RecordDetail detail = interviewRecord.getDetails().get(detailIndex);
                detail.setQuestion(recordDetailUpdateDTO.getQuestion());
                detail.setAnswer(recordDetailUpdateDTO.getAnswer());
                // updatedAt 갱신
                interviewRecord.setUpdatedAt(new Date());
                recordRepository.save(record);
                return recordMapper.toRecordDetailResponse(detail);
            }
        }
        return null;
    }

    @Override
    public boolean deleteRecordDetail(String userId, String interviewRecordId, int detailIndex) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.InterviewRecord interviewRecord = findInterviewRecordById(record, interviewRecordId);
            if (interviewRecord != null && isValidIndex(detailIndex, interviewRecord.getDetails().size())) {
                interviewRecord.getDetails().remove(detailIndex);
                // updatedAt 갱신
                interviewRecord.setUpdatedAt(new Date());
                recordRepository.save(record);
                return true;
            }
        }
        return false;
    }

    // 사이드바 데이터 조회
    @Override
    public List<InterviewRecordSidebarDTO> getSidebarData(String userId) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            List<Record.InterviewRecord> interviewRecords = optionalRecord.get().getRecords();
            return interviewRecords.stream()
                    .map(recordMapper::toInterviewRecordSidebarDTO)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    // 헬퍼 메서드

    private Record.InterviewRecord findInterviewRecordById(Record record, String interviewRecordId) {
        return record.getRecords().stream()
                .filter(ir -> ir.getInterviewRecordId().equals(interviewRecordId))
                .findFirst()
                .orElse(null);
    }

    private boolean isValidIndex(int index, int size) {
        return index >= 0 && index < size;
    }

}
