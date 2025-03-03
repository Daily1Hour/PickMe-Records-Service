package pickme.record.service;

import pickme.record.dto.*;

import java.util.List;

public interface RecordService {

    // InterviewRecord 관련 메서드
    InterviewRecordResponseDTO createInterviewRecord(String userId, InterviewRecordCreateDTO interviewRecordCreateDTO);

    InterviewRecordResponseDTO getInterviewRecordById(String userId, String interviewRecordId, int page, int size);

    InterviewRecordResponseDTO updateInterviewRecord(String userId, String interviewRecordId, InterviewRecordUpdateDTO interviewRecordUpdateDTO);

    boolean deleteInterviewRecord(String userId, String interviewRecordId);

    // RecordDetail 관련 메서드
    RecordDetailResponseDTO createRecordDetail(String userId, String interviewRecordId, RecordDetailCreateDTO recordDetailCreateDTO);

    RecordDetailResponseDTO updateRecordDetail(String userId, String interviewRecordId, int detailIndex, RecordDetailUpdateDTO recordDetailUpdateDTO);

    boolean deleteRecordDetail(String userId, String interviewRecordId, int detailIndex);

    // 사이드바 데이터 조회
    List<InterviewRecordSidebarDTO> getSidebarData(String userId);

}
