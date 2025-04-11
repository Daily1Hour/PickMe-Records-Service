package pickme.record.service;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pickme.record.dto.*;
import pickme.record.mapper.RecordMapper;
import pickme.record.model.Record;
import pickme.record.repository.RecordRepository;

/**
 * RecordServiceImpl 클래스는 RecordService 인터페이스를 구현하며,
 * 사용자 인터뷰 기록 관리와 관련된 다양한 기능을 제공합니다.
 * 
 * <h3>주요 기능:</h3>
 * <ul>
 * <li>인터뷰 기록 생성, 조회, 업데이트, 삭제</li>
 * <li>인터뷰 기록의 세부 항목 생성, 조회, 업데이트, 삭제</li>
 * <li>사이드바 데이터 조회</li>
 * </ul>
 * 
 * <p>
 * 이 클래스는 RecordRepository와 RecordMapper를 사용하여
 * 데이터베이스와의 상호작용 및 데이터 변환 작업을 수행합니다.
 * 또한 SLF4J Logger를 사용하여 애플리케이션 실행 중 발생하는 이벤트를 기록합니다.
 * </p>
 * 
 * <h3>주요 메서드:</h3>
 * <ul>
 * <li><b>createInterviewRecord:</b> 새로운 인터뷰 기록을 생성합니다.</li>
 * <li><b>getInterviewRecordById:</b> 특정 인터뷰 기록을 조회합니다.</li>
 * <li><b>updateInterviewRecord:</b> 인터뷰 기록을 업데이트합니다.</li>
 * <li><b>deleteInterviewRecord:</b> 인터뷰 기록을 삭제합니다.</li>
 * <li><b>createRecordDetail:</b> 인터뷰 기록의 세부 항목을 생성합니다.</li>
 * <li><b>updateRecordDetail:</b> 인터뷰 기록의 세부 항목을 업데이트합니다.</li>
 * <li><b>deleteRecordDetail:</b> 인터뷰 기록의 세부 항목을 삭제합니다.</li>
 * <li><b>getSidebarData:</b> 사용자 ID를 기반으로 사이드바 데이터를 조회합니다.</li>
 * </ul>
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordMapper recordMapper;

    /**
     * RecordServiceImpl 클래스의 로깅을 담당하는 Logger 객체입니다.
     * 이 Logger는 SLF4J의 LoggerFactory를 사용하여 생성되며,
     * 애플리케이션 실행 중 발생하는 이벤트를 기록하는 데 사용됩니다.
     */
    private static final Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);

    /**
     * 주어진 사용자 ID와 인터뷰 기록 생성 DTO를 사용하여 새로운 인터뷰 기록을 생성합니다.
     * 
     * @param userId                   인터뷰 기록을 생성할 사용자의 ID
     * @param interviewRecordCreateDTO 생성할 인터뷰 기록의 정보를 담고 있는 DTO
     * @return 생성된 인터뷰 기록의 응답 DTO
     * 
     *         이 메서드는 다음과 같은 작업을 수행합니다:
     *         1. 주어진 사용자 ID로 기존 Record를 조회합니다.
     *         - 만약 Record가 존재하지 않으면 새로운 Record를 생성합니다.
     *         2. 새로운 InterviewRecord 객체를 생성하고, 주어진 DTO의 데이터를 설정합니다.
     *         3. 생성된 InterviewRecord를 Record에 추가합니다.
     *         4. Record를 저장소에 저장합니다.
     *         5. 생성된 InterviewRecord를 응답 DTO로 변환하여 반환합니다.
     */
    @Override
    public InterviewRecordResponseDTO createInterviewRecord(String userId,
            InterviewRecordCreateDTO interviewRecordCreateDTO) {
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

    /**
     * 주어진 사용자 ID와 인터뷰 기록 ID를 기반으로 인터뷰 기록을 조회합니다.
     * 
     * @param userId            조회할 사용자의 ID
     * @param interviewRecordId 조회할 인터뷰 기록의 ID
     * @param page              페이징 처리를 위한 페이지 번호 (0부터 시작)
     * @param size              페이징 처리를 위한 페이지 크기
     * @return 인터뷰 기록에 대한 응답 DTO 객체.
     *         유효하지 않은 페이징 매개변수나 데이터를 찾을 수 없는 경우 null을 반환합니다.
     */
    @Override
    public InterviewRecordResponseDTO getInterviewRecordById(
            String userId, String interviewRecordId, int page, int size) {
        Optional<Record> optionalRecord = recordRepository.findById(userId);
        if (optionalRecord.isPresent()) {
            Record.InterviewRecord interviewRecord = findInterviewRecordById(optionalRecord.get(), interviewRecordId);

            if (interviewRecord != null) {
                // RecordDetail에 페이징 적용
                List<Record.RecordDetail> allDetails = interviewRecord.getDetails();

                if (allDetails == null || allDetails.isEmpty()) {
                    // details가 없으면 빈 리스트로 반환
                    InterviewRecordResponseDTO responseDTO = recordMapper.toInterviewRecordResponse(interviewRecord);
                    responseDTO.setDetails(Collections.emptyList());
                }

                // RecordDetail에 페이징 적용
                int start = page * size;
                int end = start + size;
                List<Record.RecordDetail> paginatedDetails;

                try {
                    paginatedDetails = allDetails.subList(start, Math.min(end, allDetails.size()));
                    InterviewRecordResponseDTO responseDTO = recordMapper.toInterviewRecordResponse(interviewRecord);
                    responseDTO.setDetails(recordMapper.toRecordDetailResponseList(paginatedDetails));
                    return responseDTO;
                } catch (IndexOutOfBoundsException e) {
                    logger.warn("Invalid pagination parameters: start={}, size={}, total={}",
                            start, size, allDetails.size(), e);
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 사용자의 인터뷰 기록을 업데이트합니다.
     *
     * @param userId                   업데이트할 인터뷰 기록이 속한 사용자의 ID
     * @param interviewRecordId        업데이트할 인터뷰 기록의 ID
     * @param interviewRecordUpdateDTO 업데이트할 인터뷰 기록의 데이터가 포함된 DTO
     * @return 업데이트된 인터뷰 기록의 응답 DTO, 기록이 없거나 업데이트에 실패한 경우 null 반환
     */
    @Override
    public InterviewRecordResponseDTO updateInterviewRecord(
            String userId, String interviewRecordId, InterviewRecordUpdateDTO interviewRecordUpdateDTO) {
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

    /**
     * 주어진 사용자 ID와 인터뷰 기록 ID를 기반으로 인터뷰 기록을 삭제합니다.
     *
     * @param userId            삭제할 인터뷰 기록이 속한 사용자의 ID
     * @param interviewRecordId 삭제할 인터뷰 기록의 ID
     * @return 인터뷰 기록이 성공적으로 삭제되었으면 true, 그렇지 않으면 false
     */
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

    /**
     * 주어진 사용자 ID와 인터뷰 기록 ID를 기반으로 새로운 기록 세부 정보를 생성합니다.
     * 
     * @param userId                사용자의 고유 식별자
     * @param interviewRecordId     인터뷰 기록의 고유 식별자
     * @param recordDetailCreateDTO 생성할 기록 세부 정보의 데이터 전송 객체
     * @return 생성된 기록 세부 정보를 포함하는 RecordDetailResponseDTO 객체,
     *         해당 인터뷰 기록이 존재하지 않거나 사용자가 존재하지 않을 경우 null 반환
     */
    @Override
    public RecordDetailResponseDTO createRecordDetail(
            String userId, String interviewRecordId, RecordDetailCreateDTO recordDetailCreateDTO) {
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

    /**
     * 주어진 사용자 ID와 인터뷰 기록 ID를 기반으로 특정 세부 정보를 업데이트합니다.
     *
     * @param userId                업데이트할 사용자의 ID
     * @param interviewRecordId     업데이트할 인터뷰 기록의 ID
     * @param detailIndex           업데이트할 세부 정보의 인덱스
     * @param recordDetailUpdateDTO 업데이트할 질문 및 답변 정보를 포함하는 DTO
     * @return 업데이트된 세부 정보의 응답 DTO,
     *         만약 기록이 존재하지 않거나 유효하지 않은 인덱스일 경우 null 반환
     */
    @Override
    public RecordDetailResponseDTO updateRecordDetail(
            String userId, String interviewRecordId, int detailIndex, RecordDetailUpdateDTO recordDetailUpdateDTO) {
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

    /**
     * 주어진 사용자 ID와 인터뷰 기록 ID, 세부 항목 인덱스를 기반으로 인터뷰 기록의 세부 항목을 삭제합니다.
     *
     * @param userId            삭제할 기록이 속한 사용자의 ID
     * @param interviewRecordId 삭제할 인터뷰 기록의 ID
     * @param detailIndex       삭제할 세부 항목의 인덱스
     * @return 삭제가 성공적으로 이루어진 경우 true, 그렇지 않은 경우 false
     * 
     *         주의:
     *         - userId에 해당하는 Record가 존재하지 않으면 false를 반환합니다.
     *         - interviewRecordId에 해당하는 InterviewRecord가 존재하지 않으면 false를 반환합니다.
     *         - detailIndex가 유효하지 않은 경우(false를 반환) 삭제가 수행되지 않습니다.
     */
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

    /**
     * 주어진 사용자 ID를 기반으로 사이드바 데이터를 가져옵니다.
     *
     * @param userId 사용자 ID
     * @return InterviewRecordSidebarDTO 객체의 리스트를 반환합니다.
     *         사용자가 존재하지 않으면 빈 리스트를 반환합니다.
     */
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

    /**
     * 주어진 Record 객체에서 특정 인터뷰 기록 ID에 해당하는 InterviewRecord를 찾습니다.
     *
     * @param record            검색할 인터뷰 기록이 포함된 Record 객체
     * @param interviewRecordId 찾고자 하는 인터뷰 기록의 ID
     * @return 주어진 ID와 일치하는 InterviewRecord 객체를 반환하며,
     *         일치하는 기록이 없을 경우 null을 반환합니다.
     */
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
