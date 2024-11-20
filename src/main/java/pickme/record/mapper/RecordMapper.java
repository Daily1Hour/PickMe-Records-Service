package pickme.record.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pickme.record.dto.RecordCreateDTO;
import pickme.record.dto.RecordResponseDTO;
import pickme.record.dto.RecordUpdateDTO;
import pickme.record.model.Record;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    Record toEntity(RecordCreateDTO dto);

    RecordResponseDTO toResponseDTO(Record record);

    void updateEntityFromDto(RecordUpdateDTO dto, @MappingTarget Record record);

}
