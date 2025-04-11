package pickme.record.service;

import java.util.List;

import pickme.record.dto.*;

/**
 * RecordService는 인터뷰 기록 및 세부 기록을 관리하기 위한 서비스 인터페이스입니다.
 */
public interface RecordService {

    /**
     * 새로운 인터뷰 기록을 생성합니다.
     *
     * @param userId                   사용자 ID
     * @param interviewRecordCreateDTO 인터뷰 기록 생성 요청 데이터
     * @return 생성된 인터뷰 기록의 응답 데이터
     */
    InterviewRecordResponseDTO createInterviewRecord(String userId, InterviewRecordCreateDTO interviewRecordCreateDTO);

    /**
     * 특정 인터뷰 기록을 ID로 조회합니다.
     *
     * @param userId            사용자 ID
     * @param interviewRecordId 인터뷰 기록 ID
     * @param page              페이지 번호
     * @param size              페이지 크기
     * @return 조회된 인터뷰 기록의 응답 데이터
     */
    InterviewRecordResponseDTO getInterviewRecordById(String userId, String interviewRecordId, int page, int size);

    /**
     * 특정 인터뷰 기록을 업데이트합니다.
     *
     * @param userId                   사용자 ID
     * @param interviewRecordId        인터뷰 기록 ID
     * @param interviewRecordUpdateDTO 인터뷰 기록 업데이트 요청 데이터
     * @return 업데이트된 인터뷰 기록의 응답 데이터
     */
    InterviewRecordResponseDTO updateInterviewRecord(String userId, String interviewRecordId,
            InterviewRecordUpdateDTO interviewRecordUpdateDTO);

    /**
     * 특정 인터뷰 기록을 삭제합니다.
     *
     * @param userId            사용자 ID
     * @param interviewRecordId 인터뷰 기록 ID
     * @return 삭제 성공 여부
     */
    boolean deleteInterviewRecord(String userId, String interviewRecordId);

    /**
     * 새로운 기록 세부 정보를 생성합니다.
     *
     * @param userId                사용자 ID
     * @param interviewRecordId     인터뷰 기록 ID
     * @param recordDetailCreateDTO 기록 세부 정보 생성 요청 데이터
     * @return 생성된 기록 세부 정보의 응답 데이터
     */
    RecordDetailResponseDTO createRecordDetail(String userId, String interviewRecordId,
            RecordDetailCreateDTO recordDetailCreateDTO);

    /**
     * 특정 기록 세부 정보를 업데이트합니다.
     *
     * @param userId                사용자 ID
     * @param interviewRecordId     인터뷰 기록 ID
     * @param detailIndex           세부 정보 인덱스
     * @param recordDetailUpdateDTO 기록 세부 정보 업데이트 요청 데이터
     * @return 업데이트된 기록 세부 정보의 응답 데이터
     */
    RecordDetailResponseDTO updateRecordDetail(String userId, String interviewRecordId, int detailIndex,
            RecordDetailUpdateDTO recordDetailUpdateDTO);

    /**
     * 특정 기록 세부 정보를 삭제합니다.
     *
     * @param userId            사용자 ID
     * @param interviewRecordId 인터뷰 기록 ID
     * @param detailIndex       세부 정보 인덱스
     * @return 삭제 성공 여부
     */
    boolean deleteRecordDetail(String userId, String interviewRecordId, int detailIndex);

    /**
     * 사이드바에 표시할 데이터를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사이드바 데이터 목록
     */
    List<InterviewRecordSidebarDTO> getSidebarData(String userId);
}
