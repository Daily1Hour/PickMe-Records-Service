package pickme.record.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pickme.record.dto.*;
import pickme.record.service.RecordService;

/**
 * RecordController는 면접 기록 관리 API를 제공하는 컨트롤러입니다.
 * 
 * <p>
 * 다음과 같은 기능을 제공합니다:
 * </p>
 * <ul>
 * <li>새로운 면접 기록 생성</li>
 * <li>특정 면접 기록 조회</li>
 * <li>면접 기록 업데이트</li>
 * <li>면접 기록 삭제</li>
 * <li>기존 면접 기록에 새로운 질문 및 답변 추가</li>
 * <li>특정 질문 및 답변 업데이트</li>
 * <li>특정 질문 및 답변 삭제</li>
 * <li>사이드바 데이터 조회</li>
 * </ul>
 * 
 * <p>
 * 사용자는 HttpServletRequest를 통해 인증된 userId를 전달받아 각 요청을 처리합니다.
 * </p>
 */
@RestController
@RequestMapping("/record")
@Tag(name = "Record", description = "면접 기록 관리 API")
public class RecordController {
    /**
     * RecordService를 주입받기 위한 필드입니다.
     * Spring의 @Autowired 어노테이션을 사용하여 의존성을 자동으로 주입합니다.
     */
    @Autowired
    private RecordService recordService;

    /**
     * 새로운 면접 기록을 생성합니다.
     *
     * @param request                  HTTP 요청 객체로, 사용자 ID를 포함합니다.
     * @param interviewRecordCreateDTO 생성할 면접 기록의 정보를 담은 DTO 객체입니다.
     * @return 생성된 면접 기록 정보를 포함한 ResponseEntity 객체를 반환합니다.
     * @throws Exception 면접 기록 생성 중 오류가 발생한 경우 예외를 던집니다.
     */
    @Operation(summary = "면접 기록 생성", description = "새로운 면접 기록을 생성합니다.")
    @PostMapping("/interview")
    public ResponseEntity<InterviewRecordResponseDTO> createInterviewRecord(
            HttpServletRequest request,
            @Valid @RequestBody InterviewRecordCreateDTO interviewRecordCreateDTO) throws Exception {
        String userId = (String) request.getAttribute("userId");
        InterviewRecordResponseDTO responseDTO = recordService.createInterviewRecord(userId, interviewRecordCreateDTO);

        return ResponseEntity.status(201).body(responseDTO);
    }

    /**
     * 특정 면접 기록을 조회합니다.
     *
     * @param request           HttpServletRequest 객체로 사용자 요청 정보를 포함합니다.
     * @param interviewRecordId 조회할 면접 기록의 ID입니다.
     * @param page              페이징 처리를 위한 페이지 번호입니다. 기본값은 0입니다.
     * @param size              페이징 처리를 위한 페이지 크기입니다. 기본값은 10입니다.
     * @return 면접 기록 정보를 포함한 ResponseEntity 객체를 반환합니다.
     *         면접 기록이 존재하지 않을 경우 404 상태 코드를 반환합니다.
     * @throws Exception 처리 중 예외가 발생할 경우 던집니다.
     */
    @Operation(summary = "면접 기록 조회", description = "특정 면접 기록을 조회합니다.")
    @GetMapping("/interview/{interviewRecordId}")
    public ResponseEntity<InterviewRecordResponseDTO> getInterviewRecord(
            HttpServletRequest request,
            @PathVariable String interviewRecordId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {
        String userId = (String) request.getAttribute("userId");
        InterviewRecordResponseDTO responseDTO = recordService.getInterviewRecordById(userId, interviewRecordId, page,
                size);

        if (responseDTO != null) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * 면접 기록을 업데이트합니다.
     * 
     * <p>
     * 이 메서드는 주어진 면접 기록 ID에 해당하는 면접 기록의 기업명과 카테고리를 업데이트합니다.
     * 요청 본문에 포함된 데이터를 기반으로 업데이트가 수행됩니다.
     * </p>
     * 
     * @param request                  클라이언트 요청 객체로, 사용자 ID를 포함합니다.
     * @param interviewRecordId        업데이트할 면접 기록의 고유 ID입니다.
     * @param interviewRecordUpdateDTO 업데이트할 데이터를 포함하는 DTO 객체입니다.
     * @return 업데이트된 면접 기록 데이터를 포함하는 ResponseEntity 객체를 반환합니다.
     *         성공 시 200 OK 상태 코드와 함께 데이터를 반환하며,
     *         면접 기록을 찾을 수 없는 경우 404 Not Found 상태 코드를 반환합니다.
     * @throws Exception 업데이트 과정에서 발생할 수 있는 예외를 나타냅니다.
     */
    @Operation(summary = "면접 기록 업데이트", description = "면접 기록의 기업명과 카테고리를 업데이트합니다.")
    @PutMapping("/interview/{interviewRecordId}")
    public ResponseEntity<InterviewRecordResponseDTO> updateInterviewRecord(
            HttpServletRequest request,
            @PathVariable String interviewRecordId,
            @Valid @RequestBody InterviewRecordUpdateDTO interviewRecordUpdateDTO) throws Exception {
        String userId = (String) request.getAttribute("userId");
        InterviewRecordResponseDTO responseDTO = recordService.updateInterviewRecord(userId, interviewRecordId,
                interviewRecordUpdateDTO);

        if (responseDTO != null) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * 면접 기록을 삭제합니다.
     * 
     * <p>
     * 지정된 면접 기록 ID에 해당하는 면접 기록과 그에 속한 모든 질문 및 답변을 삭제합니다.
     * </p>
     * 
     * @param request           HTTP 요청 객체로, 사용자 ID를 포함합니다.
     * @param interviewRecordId 삭제할 면접 기록의 ID.
     * @return 삭제 성공 시 204 No Content 응답, 실패 시 404 Not Found 응답.
     * @throws Exception 삭제 과정에서 발생할 수 있는 예외.
     */
    @Operation(summary = "면접 기록 삭제", description = "면접 기록과 그에 속한 모든 질문과 답변을 삭제합니다.")
    @DeleteMapping("/interview/{interviewRecordId}")
    public ResponseEntity<Void> deleteInterviewRecord(
            HttpServletRequest request,
            @PathVariable String interviewRecordId) throws Exception {
        String userId = (String) request.getAttribute("userId");
        boolean deleted = recordService.deleteInterviewRecord(userId, interviewRecordId);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * 기존 면접 기록에 새로운 질문과 답변을 추가합니다.
     *
     * @param request               HttpServletRequest 객체로 사용자 요청 정보를 포함합니다.
     * @param interviewRecordId     추가할 질문 및 답변이 속한 면접 기록의 ID입니다.
     * @param recordDetailCreateDTO 새로운 질문 및 답변 정보를 포함하는 DTO 객체입니다.
     * @return 생성된 질문 및 답변 정보를 포함하는 ResponseEntity 객체를 반환합니다.
     *         성공 시 HTTP 상태 코드 201(Created)을 반환하며,
     *         실패 시 HTTP 상태 코드 404(Not Found)를 반환합니다.
     * @throws Exception 처리 중 예외가 발생할 경우 예외를 던집니다.
     */
    @Operation(summary = "질문 및 답변 추가", description = "기존 면접 기록에 새로운 질문과 답변을 추가합니다.")
    @PostMapping("/interview/{interviewRecordId}/detail")
    public ResponseEntity<RecordDetailResponseDTO> createRecordDetail(
            HttpServletRequest request,
            @PathVariable String interviewRecordId,
            @Valid @RequestBody RecordDetailCreateDTO recordDetailCreateDTO) throws Exception {
        String userId = (String) request.getAttribute("userId");
        RecordDetailResponseDTO responseDTO = recordService.createRecordDetail(userId, interviewRecordId,
                recordDetailCreateDTO);

        if (responseDTO != null) {
            return ResponseEntity.status(201).body(responseDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * 특정 면접 기록의 질문 및 답변을 업데이트합니다.
     *
     * @param request               HttpServletRequest 객체로 사용자 요청 정보를 포함합니다.
     * @param interviewRecordId     업데이트할 면접 기록의 ID입니다.
     * @param detailIndex           업데이트할 질문/답변의 인덱스입니다.
     * @param recordDetailUpdateDTO 업데이트할 질문 및 답변 정보를 포함하는 DTO 객체입니다.
     * @return 업데이트된 질문 및 답변 정보를 포함하는 ResponseEntity 객체를 반환합니다.
     *         성공 시 200 OK와 함께 데이터를 반환하며, 실패 시 404 Not Found를 반환합니다.
     * @throws Exception 처리 중 예외가 발생할 경우 던져질 수 있습니다.
     */
    @Operation(summary = "질문 및 답변 업데이트", description = "면접 기록의 특정 질문과 답변을 업데이트합니다.")
    @PutMapping("/interview/{interviewRecordId}/detail/{detailIndex}")
    public ResponseEntity<RecordDetailResponseDTO> updateRecordDetail(
            HttpServletRequest request,
            @PathVariable String interviewRecordId,
            @PathVariable int detailIndex,
            @Valid @RequestBody RecordDetailUpdateDTO recordDetailUpdateDTO) throws Exception {
        String userId = (String) request.getAttribute("userId");
        RecordDetailResponseDTO responseDTO = recordService.updateRecordDetail(userId, interviewRecordId, detailIndex,
                recordDetailUpdateDTO);

        if (responseDTO != null) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * 특정 면접 기록의 질문 및 답변을 삭제합니다.
     *
     * @param request           HTTP 요청 객체로, 사용자 ID를 포함합니다.
     * @param interviewRecordId 삭제할 면접 기록의 ID입니다.
     * @param detailIndex       삭제할 질문 및 답변의 인덱스입니다.
     * @return 삭제 성공 시 204 No Content 응답을 반환하며,
     *         삭제할 항목이 없을 경우 404 Not Found 응답을 반환합니다.
     * @throws Exception 처리 중 예외가 발생할 경우 던집니다.
     */
    @Operation(summary = "질문 및 답변 삭제", description = "면접 기록의 특정 질문과 답변을 삭제합니다.")
    @DeleteMapping("/interview/{interviewRecordId}/detail/{detailIndex}")
    public ResponseEntity<Void> deleteRecordDetail(
            HttpServletRequest request,
            @PathVariable String interviewRecordId,
            @PathVariable int detailIndex) throws Exception {
        String userId = (String) request.getAttribute("userId");
        boolean deleted = recordService.deleteRecordDetail(userId, interviewRecordId, detailIndex);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * 사이드바 데이터 조회 API.
     * <p>
     * 이 메서드는 사이드바에 필요한 면접 기록 정보를 조회합니다.
     * 클라이언트 요청에서 사용자 ID를 추출하여 해당 사용자의 면접 기록 데이터를 반환합니다.
     * </p>
     *
     * @param request HttpServletRequest 객체로, 요청의 사용자 ID를 포함합니다.
     * @return ResponseEntity 객체로, 면접 기록 정보를 담은 List를 반환합니다.
     * @throws Exception 데이터 조회 중 예외가 발생할 경우 던집니다.
     */
    @Operation(summary = "사이드바 데이터 조회", description = "사이드바에 필요한 면접 기록 정보를 조회합니다.")
    @GetMapping("/sidebar")
    public ResponseEntity<List<InterviewRecordSidebarDTO>> getSidebarData(
            HttpServletRequest request) throws Exception {
        String userId = (String) request.getAttribute("userId");
        List<InterviewRecordSidebarDTO> responseDTOs = recordService.getSidebarData(userId);

        return ResponseEntity.ok(responseDTOs);
    }
}
