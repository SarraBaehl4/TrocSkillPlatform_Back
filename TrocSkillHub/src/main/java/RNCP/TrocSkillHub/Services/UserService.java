package RNCP.TrocSkillHub.Services;

import RNCP.TrocSkillHub.DTOs.UserDTO;
import RNCP.TrocSkillHub.DTOs.UserCardDTO;
import RNCP.TrocSkillHub.Models.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface UserService {

    // CRUD de base
    User createUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User updateAvatar(Long userId, String avatarId);
    
    // Méthodes spécifiques basées sur le repository
    Optional<User> getUserByEmail(String email);
    boolean existsByEmail(String email);
    List<User> getUsersByCity(String city);
    List<User> getUsersByCountry(String country);
    UserDTO buildUserDTO(User user);
    Page<UserCardDTO> getUserCards(int page, int size);
    
}
