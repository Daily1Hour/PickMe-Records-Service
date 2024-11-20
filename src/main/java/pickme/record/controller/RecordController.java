package pickme.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pickme.record.dto.RecordCreateDTO;
import pickme.record.dto.RecordResponseDTO;
import pickme.record.dto.RecordUpdateDTO;
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
    public ResponseEntity<RecordResponseDTO> createRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestBody RecordCreateDTO recordCreateDTO
            ) {
        // 로그인 토큰 출력
        System.out.println("Received token: " + token);

        Record record = recordMapper.toEntity(recordCreateDTO);
        record.setCreatedAt(new Date());

        Record savedRecord = recordRepository.save(record);

        RecordResponseDTO responseDTO = recordMapper.toResponseDTO(savedRecord);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // READ ALL: 모든 기록 조회
    @Operation(summary = "모든 기록 조회", description = "모든 면접 기록을 조회합니다.")
    @GetMapping("/read")
    public ResponseEntity<List<RecordResponseDTO>> getAllRecords(
            @RequestHeader(value = "Authorization", required = true) String token
    ) {
        System.out.println("Received token: " + token);

        List<Record> records = recordRepository.findAll();
        List<RecordResponseDTO> responseDTOs = records.stream()
                .map(recordMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    // READ ONE: 특정 기록 조회
    @Operation(summary = "특정 기록 조회", description = "특정 면접 기록을 조회합니다.")
    @GetMapping("/read/{postId}")
    public ResponseEntity<RecordResponseDTO> getRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable String postId
    ) {
        System.out.println("Received token: " + token);

        ObjectId objectId;
        try {
            objectId = new ObjectId(postId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 잘못된 ObjectId
        }

        Optional<Record> optionalRecord = recordRepository.findById(objectId);
        if (optionalRecord.isPresent()) {
            RecordResponseDTO responseDTO = recordMapper.toResponseDTO(optionalRecord.get());
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // UPDATE: 기록 업데이트
    @Operation(summary = "기록 업데이트", description = "특정 면접 기록을 업데이트합니다.")
    @PutMapping("/update/{postId}")
    public ResponseEntity<RecordResponseDTO> updateRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable ObjectId postId,
            @RequestBody RecordUpdateDTO updatedRecordDTO
            ) {
        System.out.println("Received token: " + token);

        Optional<Record> optionalRecord = recordRepository.findById(postId);
        if (optionalRecord.isPresent()) {
            Record record = optionalRecord.get();
            recordMapper.updateEntityFromDto(updatedRecordDTO, record);
            Record savedRecord = recordRepository.save(record);
            RecordResponseDTO responseDTO = recordMapper.toResponseDTO(savedRecord);
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE: 기록 삭제
    @Operation(summary = "기록 삭제", description = "특정 면접 기록을 삭제합니다.")
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deleteRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable String postId
    ) {
        System.out.println("Received token: " + token);

        ObjectId objectId;
        try {
            objectId = new ObjectId(postId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (recordRepository.existsById(objectId)) {
            recordRepository.deleteById(objectId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
