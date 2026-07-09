package RNCP.TrocSkillHub.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import RNCP.TrocSkillHub.DTOs.UserKnowledgeDTO;
import RNCP.TrocSkillHub.Models.UserKnowledge;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserKnowledgeMapper {

    @Mapping(source = "knowledge.id", target = "knowledgeId")
    @Mapping(source = "knowledge.name", target = "knowledgeName")
    @Mapping(source = "knowledge.category.id", target = "categoryId")
    @Mapping(source = "knowledge.category.name", target = "categoryName")
    UserKnowledgeDTO toDTO(UserKnowledge userKnowledge);

    List<UserKnowledgeDTO> toDTOList(List<UserKnowledge> userKnowledges);
}