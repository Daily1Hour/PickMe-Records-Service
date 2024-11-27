package pickme.record.service;

import pickme.record.dto.*;

import java.util.Date;
import java.util.List;

public interface RecordService {

    // InterviewRecord 관련 메서드
    InterviewRecordResponseDTO createInterviewRecord(String userId, InterviewRecordCreateDTO interviewRecordCreateDTO);

    InterviewRecordResponseDTO getInterviewRecord(String userId, String enterpriseName, String category, Date createdAt, int page, int size);

    InterviewRecordResponseDTO updateInterviewRecord(String userId, String enterpriseName, String category, Date createdAt, InterviewRecordUpdateDTO interviewRecordUpdateDTO);

    boolean deleteInterviewRecord(String userId, String enterpriseName, String category, Date createdAt);

    // RecordDetail 관련 메서드
    RecordDetailResponseDTO createRecordDetail(String userId, String enterpriseName, String category, Date createdAt, RecordDetailCreateDTO recordDetailCreateDTO);

    RecordDetailResponseDTO updateRecordDetail(String userId, String enterpriseName, String category, Date createdAt, int detailIndex, RecordDetailUpdateDTO recordDetailUpdateDTO);

    boolean deleteRecordDetail(String userId, String enterpriseName, String category, Date createdAt, int detailIndex);

    // 사이드바 데이터 조회
    List<InterviewRecordSidebarDTO> getSidebarData(String userId);

}
