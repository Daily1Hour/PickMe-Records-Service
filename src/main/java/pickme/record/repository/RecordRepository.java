package pickme.record.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pickme.record.model.Record;

@Repository
public interface RecordRepository extends MongoRepository<Record, String> {
}
