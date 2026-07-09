package RNCP.TrocSkillHub.DTOs;

import RNCP.TrocSkillHub.Models.Enums.KnowledgeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKnowledgeDTO {
    private Long id;
    private Long knowledgeId;
    private String knowledgeName;
    private Long categoryId;
    private String categoryName;
    private String level;
    private KnowledgeType type;
}
