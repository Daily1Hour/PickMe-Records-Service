package pickme.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pickme.record.dto.*;
import pickme.record.service.RecordService;
import pickme.record.service.JWTService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/records")
@Tag(name = "Record", description = "면접 기록 관리 API")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private JWTService JWTService;

    // 1. 새로운 InterviewRecord 생성
    @Operation(summary = "면접 기록 생성", description = "새로운 면접 기록을 생성합니다.")
    @PostMapping("/interview")
    public ResponseEntity<InterviewRecordResponseDTO> createInterviewRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @Valid @RequestBody InterviewRecordCreateDTO interviewRecordCreateDTO
    ) throws Exception {
        String userId = JWTService.extractToken(token);
        InterviewRecordResponseDTO responseDTO = recordService.createInterviewRecord(userId, interviewRecordCreateDTO);
        return ResponseEntity.status(201).body(responseDTO);
    }

    // 2. 특정 InterviewRecord 조회
    @Operation(summary = "면접 기록 조회", description = "특정 면접 기록을 조회합니다.")
    @GetMapping("/interview")
    public ResponseEntity<InterviewRecordResponseDTO> getInterviewRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestParam String enterpriseName,
            @RequestParam String category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date createdAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws Exception {
        String userId = JWTService.extractToken(token);
        InterviewRecordResponseDTO responseDTO = recordService.getInterviewRecord(userId, enterpriseName, category, createdAt, page, size);
        if (responseDTO != null) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    // 3. InterviewRecord 업데이트
    @Operation(summary = "면접 기록 업데이트", description = "면접 기록의 기업명과 카테고리를 업데이트합니다.")
    @PutMapping("/interview")
    public ResponseEntity<InterviewRecordResponseDTO> updateInterviewRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestParam String enterpriseName,
            @RequestParam String category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date createdAt,
            @Valid @RequestBody InterviewRecordUpdateDTO interviewRecordUpdateDTO
    ) throws Exception {
        String userId = JWTService.extractToken(token);
        InterviewRecordResponseDTO responseDTO = recordService.updateInterviewRecord(userId, enterpriseName, category, createdAt, interviewRecordUpdateDTO);
        if (responseDTO != null) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    // 4. InterviewRecord 삭제
    @Operation(summary = "면접 기록 삭제", description = "면접 기록과 그에 속한 모든 질문과 답변을 삭제합니다.")
    @DeleteMapping("/interview")
    public ResponseEntity<Void> deleteInterviewRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestParam String enterpriseName,
            @RequestParam String category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date createdAt
    ) throws Exception {
        String userId = JWTService.extractToken(token);
        boolean deleted = recordService.deleteInterviewRecord(userId, enterpriseName, category, createdAt);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    // 5. 새로운 RecordDetail 추가
    @Operation(summary = "질문 및 답변 추가", description = "기존 면접 기록에 새로운 질문과 답변을 추가합니다.")
    @PostMapping("/interview/detail")
    public ResponseEntity<RecordDetailResponseDTO> createRecordDetail(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestParam String enterpriseName,
            @RequestParam String category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date createdAt,
            @Valid @RequestBody RecordDetailCreateDTO recordDetailCreateDTO
    ) throws Exception {
        String userId = JWTService.extractToken(token);
        RecordDetailResponseDTO responseDTO = recordService.createRecordDetail(userId, enterpriseName, category, createdAt, recordDetailCreateDTO);
        return ResponseEntity.status(201).body(responseDTO);
    }

    // 6. 특정 RecordDetail 업데이트
    @Operation(summary = "질문 및 답변 업데이트", description = "면접 기록의 특정 질문과 답변을 업데이트합니다.")
    @PutMapping("/interview/detail")
    public ResponseEntity<RecordDetailResponseDTO> updateRecordDetail(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestParam String enterpriseName,
            @RequestParam String category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date createdAt,
            @RequestParam int detailIndex,
            @Valid @RequestBody RecordDetailUpdateDTO recordDetailUpdateDTO
    ) throws Exception {
        String userId = JWTService.extractToken(token);
        RecordDetailResponseDTO responseDTO = recordService.updateRecordDetail(userId, enterpriseName, category, createdAt, detailIndex, recordDetailUpdateDTO);
        if (responseDTO != null) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    // 7. 특정 RecordDetail 삭제
    @Operation(summary = "질문 및 답변 삭제", description = "면접 기록의 특정 질문과 답변을 삭제합니다.")
    @DeleteMapping("/interview/detail")
    public ResponseEntity<Void> deleteRecordDetail(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestParam String enterpriseName,
            @RequestParam String category,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date createdAt,
            @RequestParam int detailIndex
    ) throws Exception {
        String userId = JWTService.extractToken(token);
        boolean deleted = recordService.deleteRecordDetail(userId, enterpriseName, category, createdAt, detailIndex);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    // 8. 사이드바 데이터 조회
    @Operation(summary = "사이드바 데이터 조회", description = "사이드바에 필요한 면접 기록 정보를 조회합니다.")
    @GetMapping("/sidebar")
    public ResponseEntity<List<InterviewRecordSidebarDTO>> getSidebarData(
            @RequestHeader(value = "Authorization", required = true) String token
    ) throws Exception {
        String userId = JWTService.extractToken(token);
        List<InterviewRecordSidebarDTO> responseDTOs = recordService.getSidebarData(userId);
        return ResponseEntity.ok(responseDTOs);
    }

}
