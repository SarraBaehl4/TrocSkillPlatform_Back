package RNCP.TrocSkillHub.Services.ImplServices;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import RNCP.TrocSkillHub.DTOs.UserCardDTO;
import RNCP.TrocSkillHub.DTOs.UserDTO;
import RNCP.TrocSkillHub.Mappers.UserMapper;
import RNCP.TrocSkillHub.Models.Enums.KnowledgeType;
import RNCP.TrocSkillHub.Models.User;
import RNCP.TrocSkillHub.Repositories.UserRepository;
import RNCP.TrocSkillHub.Services.UserService;
import RNCP.TrocSkillHub.Utils.AvatarConstants;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setAddress(user.getAddress());
                    existingUser.setEmail(user.getEmail());
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

    @Override
    public Page<UserCardDTO> getUserCards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAllWithAtLeastOneSkill(KnowledgeType.SKILL, pageable);
        return users.map(this::toUserCardDTO);
    }

    private UserCardDTO toUserCardDTO(User user) {
        UserDTO fullDTO = userMapper.toDTO(user);

        UserCardDTO card = new UserCardDTO();
        card.setId(fullDTO.getId());
        card.setPseudo(fullDTO.getFirstName() + " " + fullDTO.getLastName());
        card.setPictureUrl(fullDTO.getPictureUrl());
        card.setCompetences(fullDTO.getCompetences());
        card.setBesoins(fullDTO.getBesoins());
        return card;
    }

    @Override
    public User updateAvatar(Long userId, String avatarId) {
        if (!AvatarConstants.isValidAvatarId(avatarId)) {
            throw new RuntimeException("Avatar invalide: " + avatarId);
        }

        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setAvatarId(avatarId);
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + userId));
    }
}