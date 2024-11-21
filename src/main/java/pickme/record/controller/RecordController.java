package pickme.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pickme.record.dto.RecordCreateDTO;
import pickme.record.dto.RecordResponseDTO;
import pickme.record.dto.RecordUpdateDTO;
import pickme.record.service.RecordService;
import pickme.record.util.TokenUtil;

@RestController
@RequestMapping("/records")
@Tag(name = "Record", description = "면접 기록 관리 API")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private TokenUtil tokenUtil;

    // CREATE: 기록 생성
    @Operation(summary = "기록 생성", description = "새로운 면접 기록을 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<RecordResponseDTO> createRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @Valid @RequestBody RecordCreateDTO recordCreateDTO
            ) throws Exception {
        String userId = tokenUtil.extractUserId(token);
        RecordResponseDTO responseDTO = recordService.createRecord(userId, recordCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // READ ALL: 모든 기록 조회
    @Operation(summary = "모든 기록 조회", description = "모든 면접 기록을 조회합니다.")
    @GetMapping("/read")
    public ResponseEntity<RecordResponseDTO> getAllRecords(
            @RequestHeader(value = "Authorization", required = true) String token
    ) throws Exception {
        String userId = tokenUtil.extractUserId(token);
        RecordResponseDTO responseDTO = recordService.getAllRecords(userId);
        return ResponseEntity.ok(responseDTO);
    }

    // READ ONE: 특정 기록 조회
    @Operation(summary = "특정 기록 조회", description = "특정 면접 기록을 조회합니다.")
    @GetMapping("/read/{enterpriseName}/{category}")
    public ResponseEntity<RecordResponseDTO.EnterpriseRecordResponse> getRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable String enterpriseName,
            @PathVariable String category
    ) throws Exception {
        String userId = tokenUtil.extractUserId(token);
        RecordResponseDTO.EnterpriseRecordResponse responseDTO = recordService.getRecord(userId, enterpriseName, category);
        if (responseDTO != null) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // UPDATE: 기록 업데이트
    @Operation(summary = "기록 업데이트", description = "특정 면접 기록을 업데이트합니다.")
    @PutMapping("/update/{enterpriseName}/{category}")
    public ResponseEntity<RecordResponseDTO.EnterpriseRecordResponse> updateRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable String enterpriseName,
            @PathVariable String category,
            @RequestBody RecordUpdateDTO updatedRecordDTO
            ) throws Exception {
        String userId = tokenUtil.extractUserId(token);
        RecordResponseDTO.EnterpriseRecordResponse responseDTO = recordService.updateRecord(userId, enterpriseName, category, updatedRecordDTO);
        if (responseDTO != null) {
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: 기록 삭제
    @Operation(summary = "기록 삭제", description = "특정 면접 기록을 삭제합니다.")
    @DeleteMapping("/delete/{enterpriseName}/{category}")
    public ResponseEntity<Void> deleteRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable String enterpriseName,
            @PathVariable String category
    ) throws Exception {
        String userId = tokenUtil.extractUserId(token);
        boolean deleted = recordService.deleteRecord(userId, enterpriseName, category);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
