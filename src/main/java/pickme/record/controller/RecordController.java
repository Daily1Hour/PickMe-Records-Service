package pickme.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pickme.record.model.Record;
import pickme.record.repository.RecordRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/record")
@Tag(name = "Record", description = "면접 기록 관리 API")
public class RecordController {

    @Autowired
    private RecordRepository recordRepository;

    // CREATE: 기록 생성
    @Operation(summary = "기록 생성", description = "새로운 면접 기록을 생성합니다.")
    @PostMapping("/create")
    public Record createRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @RequestBody Record record
    ) {
        // 로그인 토큰 출력
        System.out.println("Received token: " + token);

        record.setCreatedAt(new Date());
        return recordRepository.save(record);
    }

    // READ ALL: 모든 기록 조회
    @Operation(summary = "모든 기록 조회", description = "모든 면접 기록을 조회합니다.")
    @GetMapping("/read")
    public List<Record> getAllRecords(
            @RequestHeader(value = "Authorization", required = true) String token
    ) {
        System.out.println("Received token: " + token);
        return recordRepository.findAll();
    }

    // READ ONE: 특정 기록 조회
    @Operation(summary = "특정 기록 조회", description = "특정 면접 기록을 조회합니다.")
    @GetMapping("/read/{postId}")
    public Optional<Record> getRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable ObjectId postId
    ) {
        System.out.println("Received token: " + token);
        return recordRepository.findById(postId);
    }

    // UPDATE: 기록 업데이트
    @Operation(summary = "기록 업데이트", description = "특정 면접 기록을 업데이트합니다.")
    @PutMapping("/update/{postId}")
    public Record updateRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable ObjectId postId,
            @RequestBody Record updatedRecord
    ) {
        System.out.println("Received token: " + token);

        return recordRepository.findById(postId)
                .map(record -> {
                    record.setUserId(updatedRecord.getUserId());
                    record.setCategory(updatedRecord.getCategory());
                    record.setContent(updatedRecord.getContent());
                    return recordRepository.save(record);
                })
                .orElseGet(() -> {
                    updatedRecord.setPostId(postId);
                    updatedRecord.setCreatedAt(new Date());
                    return recordRepository.save(updatedRecord);
                });
    }

    // DELETE: 기록 삭제
    @Operation(summary = "기록 삭제", description = "특정 면접 기록을 삭제합니다.")
    @DeleteMapping("/delete/{postId}")
    public void deleteRecord(
            @RequestHeader(value = "Authorization", required = true) String token,
            @PathVariable ObjectId postId
    ) {
        System.out.println("Received token: " + token);
        recordRepository.deleteById(postId);
    }

}
