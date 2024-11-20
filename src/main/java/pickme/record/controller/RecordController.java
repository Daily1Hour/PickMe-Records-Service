package pickme.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pickme.record.dto.RecordCreateDto;
import pickme.record.dto.RecordResponseDto;
import pickme.record.dto.RecordUpdateDto;
import pickme.record.mapper.RecordMapper;
import pickme.record.model.Record;
import pickme.record.repository.RecordRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/records")
@Tag(name = "Record", description = "면접 기록 관리 API")
public class RecordController {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private RecordMapper recordMapper;

    // CREATE: 기록 생성
    @Operation(summary = "기록 생성", description = "새로운 면접 기록을 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<RecordResponseDto> createRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestBody RecordCreateDto recordCreateDto
            ) {
        // 로그인 토큰 출력
        System.out.println("Received token: " + token);

        Record record = recordMapper.toEntity(recordCreateDto);
        record.setCreatedAt(new Date());

        Record savedRecord = recordRepository.save(record);

        RecordResponseDto responseDto = recordMapper.toResponseDto(savedRecord);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // READ ALL: 모든 기록 조회
    @Operation(summary = "모든 기록 조회", description = "모든 면접 기록을 조회합니다.")
    @GetMapping("/read")
    public ResponseEntity<List<RecordResponseDto>> getAllRecords(
            @RequestHeader(value = "Authorization", required = true) String token
    ) {
        System.out.println("Received token: " + token);

        List<Record> records = recordRepository.findAll();
        List<RecordResponseDto> responseDtos = records.stream()
                .map(recordMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    // READ ONE: 특정 기록 조회
    @Operation(summary = "특정 기록 조회", description = "특정 면접 기록을 조회합니다.")
    @GetMapping("/read/{postId}")
    public ResponseEntity<RecordResponseDto> getRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable ObjectId postId
    ) {
        System.out.println("Received token: " + token);

        Optional<Record> optionalRecord = recordRepository.findById(postId);
        if (optionalRecord.isPresent()) {
            RecordResponseDto responseDto = recordMapper.toResponseDto(optionalRecord.get());
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // UPDATE: 기록 업데이트
    @Operation(summary = "기록 업데이트", description = "특정 면접 기록을 업데이트합니다.")
    @PutMapping("/update/{postId}")
    public ResponseEntity<RecordResponseDto> updateRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable ObjectId postId,
            @RequestBody RecordUpdateDto updatedRecordDto
            ) {
        System.out.println("Received token: " + token);

        Optional<Record> optionalRecord = recordRepository.findById(postId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            recordMapper.updateEntityFromDto(updatedRecordDto, record);
            Record savedRecord = recordRepository.save(record);
            RecordResponseDto responseDto = recordMapper.toResponseDto(savedRecord);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE: 기록 삭제
    @Operation(summary = "기록 삭제", description = "특정 면접 기록을 삭제합니다.")
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deleteRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable ObjectId postId
    ) {
        System.out.println("Received token: " + token);

        if (recordRepository.existsById(postId)) {
            recordRepository.deleteById(postId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
