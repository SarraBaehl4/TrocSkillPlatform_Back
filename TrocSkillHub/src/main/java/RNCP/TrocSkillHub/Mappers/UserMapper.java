package RNCP.TrocSkillHub.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import RNCP.TrocSkillHub.DTOs.EducationDTO;
import RNCP.TrocSkillHub.DTOs.ExperienceDTO;
import RNCP.TrocSkillHub.DTOs.ProjectDTO;
import RNCP.TrocSkillHub.DTOs.UserDTO;
import RNCP.TrocSkillHub.DTOs.UserKnowledgeDTO;
import RNCP.TrocSkillHub.Models.Education;
import RNCP.TrocSkillHub.Models.Enums.KnowledgeType;
import RNCP.TrocSkillHub.Models.Experience;
import RNCP.TrocSkillHub.Models.Project;
import RNCP.TrocSkillHub.Models.User;
import RNCP.TrocSkillHub.Models.UserKnowledge;
import RNCP.TrocSkillHub.Repositories.UserKnowledgeRepository;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = { UserKnowledgeMapper.class }
)
public abstract class UserMapper {

    private static final String AVATAR_BASE_PATH = "/avatars/";
    private static final String DICEBEAR_INITIALS_URL = "https://api.dicebear.com/9.x/initials/svg?seed=";

    @Autowired
    protected UserKnowledgeRepository userKnowledgeRepository;

    @Autowired
    protected UserKnowledgeMapper userKnowledgeMapper;

    public abstract UserDTO toDTO(User user);

    public abstract EducationDTO toDTO(Education education);

    public abstract ExperienceDTO toDTO(Experience experience);

    public abstract ProjectDTO toDTO(Project project);

    public abstract User toEntity(UserDTO userDTO);

    public abstract Education toEntity(EducationDTO educationDTO);

    public abstract Experience toEntity(ExperienceDTO experienceDTO);

    public abstract Project toEntity(ProjectDTO projectDTO);

    public abstract List<UserDTO> toDTOList(List<User> users);

    @AfterMapping
    protected void enrichWithKnowledgesAndPicture(User user, @MappingTarget UserDTO dto) {
        List<UserKnowledge> userKnowledges = userKnowledgeRepository.findByUserId(user.getId());

        List<UserKnowledgeDTO> competences = userKnowledges.stream()
                .filter(uk -> uk.getType() == KnowledgeType.SKILL)
                .map(userKnowledgeMapper::toDTO)
                .collect(Collectors.toList());

        List<UserKnowledgeDTO> besoins = userKnowledges.stream()
                .filter(uk -> uk.getType() == KnowledgeType.NEED)
                .map(userKnowledgeMapper::toDTO)
                .collect(Collectors.toList());

        dto.setCompetences(competences);
        dto.setBesoins(besoins);
        dto.setPictureUrl(buildPictureUrl(user));
    }

    private String buildPictureUrl(User user) {
        if (user.getAvatarId() != null) {
            return AVATAR_BASE_PATH + user.getAvatarId() + ".svg";
        }
        String pseudo = user.getFirstName() + " " + user.getLastName();
        return DICEBEAR_INITIALS_URL + pseudo;
    }
}