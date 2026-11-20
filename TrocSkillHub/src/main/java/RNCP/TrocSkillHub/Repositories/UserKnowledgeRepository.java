package RNCP.TrocSkillHub.Repositories;

import RNCP.TrocSkillHub.Models.UserKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserKnowledgeRepository extends JpaRepository<UserKnowledge, Long> {
    List<UserKnowledge> findByUserId(Long userId);
}