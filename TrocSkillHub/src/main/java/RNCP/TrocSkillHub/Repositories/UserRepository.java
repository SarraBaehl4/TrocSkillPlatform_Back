package RNCP.TrocSkillHub.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import RNCP.TrocSkillHub.Models.User;
import RNCP.TrocSkillHub.Models.Enums.KnowledgeType;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findByCity(String city);

    List<User> findByCountry(String country);

    @Query("SELECT DISTINCT u FROM User u JOIN UserKnowledge uk ON uk.user = u WHERE uk.type = :type")
    Page<User> findAllWithAtLeastOneSkill(@Param("type") KnowledgeType type, Pageable pageable);
}
