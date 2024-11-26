package pickme.record.service;

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
    public InterviewRecordResponseDTO getInterviewRecord(String userId, String enterpriseName, String category, Date createdAt, int page, int size) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record.InterviewRecord interviewRecord = findInterviewRecord(optionalRecord.get(), enterpriseName, category, createdAt);
            if (interviewRecord != null) {
                // RecordDetail에 페이징 적용
                List<Record.RecordDetail> allDetails = interviewRecord.getDetails();
                int start = page * size;
                int end = Math.min(start + size, allDetails.size());
                if (start >= allDetails.size()) {
                    return null; // 유효하지 않은 페이지 요청
                }
                List<Record.RecordDetail> paginatedDetails = allDetails.subList(start, end);
                InterviewRecordResponseDTO responseDTO = recordMapper.toInterviewRecordResponse(interviewRecord);
                responseDTO.setDetails(recordMapper.toRecordDetailResponseList(paginatedDetails));
                return responseDTO;
            }
        }
        return null;
    }

    @Override
    public InterviewRecordResponseDTO updateInterviewRecord(String userId, String enterpriseName, String category, Date createdAt, InterviewRecordUpdateDTO interviewRecordUpdateDTO) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.InterviewRecord interviewRecord = findInterviewRecord(record, enterpriseName, category, createdAt);
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
    public boolean deleteInterviewRecord(String userId, String enterpriseName, String category, Date createdAt) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.InterviewRecord interviewRecord = findInterviewRecord(record, enterpriseName, category, createdAt);
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
    public RecordDetailResponseDTO createRecordDetail(String userId, String enterpriseName, String category, Date createdAt, RecordDetailCreateDTO recordDetailCreateDTO) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.InterviewRecord interviewRecord = findInterviewRecord(record, enterpriseName, category, createdAt);
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
    public RecordDetailResponseDTO updateRecordDetail(String userId, String enterpriseName, String category, Date createdAt, int detailIndex, RecordDetailUpdateDTO recordDetailUpdateDTO) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.InterviewRecord interviewRecord = findInterviewRecord(record, enterpriseName, category, createdAt);
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
    public boolean deleteRecordDetail(String userId, String enterpriseName, String category, Date createdAt, int detailIndex) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            Record.InterviewRecord interviewRecord = findInterviewRecord(record, enterpriseName, category, createdAt);
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

    private Record.InterviewRecord findInterviewRecord(Record record, String enterpriseName, String category, Date createdAt) {
        return record.getRecords().stream()
                .filter(ir -> ir.getEnterpriseName().equals(enterpriseName)
                        && ir.getCategory().equals(category)
                        && ir.getCreatedAt().equals(createdAt))
                .findFirst()
                .orElse(null);
    }

    private boolean isValidIndex(int index, int size) {
        return index >= 0 && index < size;
    }

}
