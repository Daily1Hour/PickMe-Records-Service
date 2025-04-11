package pickme.record.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pickme.record.model.Record;

/**
 * RecordRepository는 MongoDB 데이터베이스와 상호작용하기 위한 리포지토리 인터페이스입니다.
 * Spring Data MongoDB의 MongoRepository를 확장하여 기본적인 CRUD 작업을 제공합니다.
 */
@Repository
public interface RecordRepository extends MongoRepository<Record, String> {
}
