package RNCP.TrocSkillHub.Services.ImplServices;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import RNCP.TrocSkillHub.DTOs.UserDTO;
import RNCP.TrocSkillHub.DTOs.UserKnowledgeDTO;
import RNCP.TrocSkillHub.Mappers.UserKnowledgeMapper;
import RNCP.TrocSkillHub.Mappers.UserMapper;
import RNCP.TrocSkillHub.Models.Enums.KnowledgeType;
import RNCP.TrocSkillHub.Models.User;
import RNCP.TrocSkillHub.Models.UserKnowledge;
import RNCP.TrocSkillHub.Repositories.UserKnowledgeRepository;
import RNCP.TrocSkillHub.Repositories.UserRepository;
import RNCP.TrocSkillHub.Services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserKnowledgeRepository userKnowledgeRepository;
    private final UserMapper userMapper;
    private final UserKnowledgeMapper userKnowledgeMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                            UserKnowledgeRepository userKnowledgeRepository,
                            UserMapper userMapper,
                            UserKnowledgeMapper userKnowledgeMapper,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userKnowledgeRepository = userKnowledgeRepository;
        this.userMapper = userMapper;
        this.userKnowledgeMapper = userKnowledgeMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Cet email existe déjà!");
        }
        validatePasswordStrength(user.getPassword());
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDTO buildUserDTO(User user) {
        UserDTO dto = userMapper.toDTO(user);

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

        return dto;
    }

    @Override
    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
            .map(existingUser -> {
                existingUser.setFirstName(user.getFirstName());
                existingUser.setLastName(user.getLastName());
                existingUser.setAddress(user.getAddress());
                existingUser.setEmail(user.getEmail());
                existingUser.setPicture(user.getPicture());
                existingUser.setCity(user.getCity());
                existingUser.setCountry(user.getCountry());
                existingUser.setPhoneNumber(user.getPhoneNumber());
                existingUser.setDescription(user.getDescription());
                existingUser.setUpdatedAt(LocalDate.now());
                return userRepository.save(existingUser);
            })
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> getUsersByCity(String city) {
        return userRepository.findByCity(city);
    }

    @Override
    public List<User> getUsersByCountry(String country) {
        return userRepository.findByCountry(country);
    }

    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("Le mot de passe doit contenir au moins 8 caractères");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new RuntimeException("Le mot de passe doit contenir au moins une majuscule");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new RuntimeException("Le mot de passe doit contenir au moins une minuscule");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new RuntimeException("Le mot de passe doit contenir au moins un chiffre");
        }
        if (!password.matches(".*[@#$%^&+=!?*].*")) {
            throw new RuntimeException("Le mot de passe doit contenir au moins un caractère spécial (@#$%^&+=!?*)");
        }
    }
}