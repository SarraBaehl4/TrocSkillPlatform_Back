package RNCP.TrocSkillHub.DTOs;

import RNCP.TrocSkillHub.Models.Enums.KnowledgeType;

public record UserKnowledgeDTO(
    Long id,
    String knowledgeName,
    String categoryName,
    String level,
    KnowledgeType type
) {}
