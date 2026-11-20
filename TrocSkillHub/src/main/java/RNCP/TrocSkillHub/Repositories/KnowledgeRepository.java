package RNCP.TrocSkillHub.Repositories;

import RNCP.TrocSkillHub.Models.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {
}