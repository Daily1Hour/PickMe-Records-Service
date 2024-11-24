package pickme.record.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pickme.record.dto.RecordCreateDto;
import pickme.record.dto.RecordResponseDto;
import pickme.record.dto.RecordUpdateDto;
import pickme.record.model.Record;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    Record toEntity(RecordCreateDto dto);

    RecordResponseDto toResponseDto(Record record);

    void updateEntityFromDto(RecordUpdateDto dto, @MappingTarget Record record);

}
