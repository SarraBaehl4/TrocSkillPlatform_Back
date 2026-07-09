package RNCP.TrocSkillHub.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import RNCP.TrocSkillHub.DTOs.KnowledgeDTO;
import RNCP.TrocSkillHub.Models.Knowledge;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KnowledgeMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    KnowledgeDTO toDTO(Knowledge knowledge);
    
    List<KnowledgeDTO> toDTOList(List<Knowledge> knowledges);
}