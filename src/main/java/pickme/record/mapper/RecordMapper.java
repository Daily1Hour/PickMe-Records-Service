package pickme.record.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pickme.record.dto.RecordCreateDTO;
import pickme.record.dto.RecordResponseDTO;
import pickme.record.dto.RecordUpdateDTO;
import pickme.record.model.Record;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    Record toEntity(RecordCreateDTO dto);

    @Mapping(target = "postId", expression = "java(record.getPostId() != null ? record.getPostId().toHexString() : null)")
    RecordResponseDTO toResponseDTO(Record record);

    @Mapping(target = "postId", ignore = true) // 업데이트 시 postId는 수정하지 않음
    void updateEntityFromDto(RecordUpdateDTO dto, @MappingTarget Record record);

}
