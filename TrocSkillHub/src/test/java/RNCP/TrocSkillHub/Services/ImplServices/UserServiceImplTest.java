package RNCP.TrocSkillHub.Services.ImplServices;

import RNCP.TrocSkillHub.DTOs.UserCardDTO;
import RNCP.TrocSkillHub.DTOs.UserDTO;
import RNCP.TrocSkillHub.Mappers.UserMapper;
import RNCP.TrocSkillHub.Models.User;
import RNCP.TrocSkillHub.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("hashedPassword");
    }

    @Test
    void createUser_ShouldHashPasswordAndSaveUser_WhenEmailDoesNotExist() {
        User inputUser = new User();
        inputUser.setEmail("new@example.com");
        inputUser.setPassword("Password123!");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("new@example.com");
        savedUser.setPassword("hashedPassword123");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Password123!")).thenReturn("hashedPassword123");
        when(userRepository.save(inputUser)).thenReturn(savedUser);

        User result = userService.createUser(inputUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPassword()).isEqualTo("hashedPassword123");

        verify(userRepository, times(1)).existsByEmail("new@example.com");
        verify(passwordEncoder, times(1)).encode("Password123!");
        verify(userRepository, times(1)).save(inputUser);
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {
        User inputUser = new User();
        inputUser.setEmail("existing@example.com");
        inputUser.setPassword("Password123!");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(inputUser))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Cet email existe déjà!");

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void createUser_ShouldThrowException_WhenPasswordTooShort() {
        User inputUser = new User();
        inputUser.setEmail("new@example.com");
        inputUser.setPassword("Pw1!");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(inputUser))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("au moins 8 caractères");

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_ShouldThrowException_WhenPasswordHasNoUppercase() {
        User inputUser = new User();
        inputUser.setEmail("new@example.com");
        inputUser.setPassword("password123!");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(inputUser))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("majuscule");
    }

    @Test
    void createUser_ShouldThrowException_WhenPasswordHasNoLowercase() {
        User inputUser = new User();
        inputUser.setEmail("new@example.com");
        inputUser.setPassword("PASSWORD123!");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(inputUser))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("minuscule");
    }

    @Test
    void createUser_ShouldThrowException_WhenPasswordHasNoDigit() {
        User inputUser = new User();
        inputUser.setEmail("new@example.com");
        inputUser.setPassword("Password!");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(inputUser))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("chiffre");
    }

    @Test
    void createUser_ShouldThrowException_WhenPasswordHasNoSpecialCharacter() {
        User inputUser = new User();
        inputUser.setEmail("new@example.com");
        inputUser.setPassword("Password123");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);

        assertThatThrownBy(() -> userService.createUser(inputUser))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("caractère spécial");
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<User> users = Arrays.asList(user, new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAllUsers();

        assertThat(result).isEmpty();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUser_WhenUserExists() {
        User inputChanges = new User();
        inputChanges.setFirstName("Jane");
        inputChanges.setLastName("Smith");
        inputChanges.setEmail("jane.smith@example.com");
        inputChanges.setAddress("123 rue Test");
        inputChanges.setCity("Paris");
        inputChanges.setCountry("France");
        inputChanges.setPhoneNumber("0600000000");
        inputChanges.setDescription("Nouvelle description");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(1L, inputChanges);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getEmail()).isEqualTo("jane.smith@example.com");
        assertThat(result.getAddress()).isEqualTo("123 rue Test");
        assertThat(result.getCity()).isEqualTo("Paris");
        assertThat(result.getCountry()).isEqualTo("France");
        assertThat(result.getPhoneNumber()).isEqualTo("0600000000");
        assertThat(result.getDescription()).isEqualTo("Nouvelle description");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        User inputChanges = new User();
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(999L, inputChanges))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Utilisateur non trouvé avec l'id: 999");

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Utilisateur non trouvé avec l'id: 999");

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenEmailExists() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("john.doe@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void getUserByEmail_ShouldReturnEmpty_WhenEmailNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail("unknown@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        boolean result = userService.existsByEmail("john.doe@example.com");

        assertThat(result).isTrue();
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        when(userRepository.existsByEmail("unknown@example.com")).thenReturn(false);

        boolean result = userService.existsByEmail("unknown@example.com");

        assertThat(result).isFalse();
    }

    @Test
    void getUsersByCity_ShouldReturnMatchingUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findByCity("Paris")).thenReturn(users);

        List<User> result = userService.getUsersByCity("Paris");

        assertThat(result).hasSize(1);
        verify(userRepository, times(1)).findByCity("Paris");
    }

    @Test
    void getUsersByCity_ShouldReturnEmptyList_WhenNoMatch() {
        when(userRepository.findByCity("Marseille")).thenReturn(Collections.emptyList());

        List<User> result = userService.getUsersByCity("Marseille");

        assertThat(result).isEmpty();
    }

    @Test
    void getUsersByCountry_ShouldReturnMatchingUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findByCountry("France")).thenReturn(users);

        List<User> result = userService.getUsersByCountry("France");

        assertThat(result).hasSize(1);
        verify(userRepository, times(1)).findByCountry("France");
    }

    @Test
    void getUsersByCountry_ShouldReturnEmptyList_WhenNoMatch() {
        when(userRepository.findByCountry("Espagne")).thenReturn(Collections.emptyList());

        List<User> result = userService.getUsersByCountry("Espagne");

        assertThat(result).isEmpty();
    }

    @Test
    void getUserCards_ShouldReturnPageOfUserCardDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Arrays.asList(user));

        UserDTO fullDTO = new UserDTO();
        fullDTO.setId(1L);
        fullDTO.setFirstName("John");
        fullDTO.setLastName("Doe");
        fullDTO.setPictureUrl("/avatars/avatar1.svg");
        fullDTO.setCompetences(Collections.emptyList());
        fullDTO.setBesoins(Collections.emptyList());

        when(userRepository.findAllWithAtLeastOneSkill(any(), any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toDTO(user)).thenReturn(fullDTO);

        Page<UserCardDTO> result = userService.getUserCards(0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPseudo()).isEqualTo("John Doe");
        assertThat(result.getContent().get(0).getPictureUrl()).isEqualTo("/avatars/avatar1.svg");
    }

    @Test
    void getUserCards_ShouldReturnEmptyPage_WhenNoUsersHaveSkills() {
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList());
        when(userRepository.findAllWithAtLeastOneSkill(any(), any(Pageable.class))).thenReturn(emptyPage);

        Page<UserCardDTO> result = userService.getUserCards(0, 10);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void updateAvatar_ShouldUpdateAndReturnUser_WhenAvatarIdIsValid() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateAvatar(1L, "avatar17");

        assertThat(result.getAvatarId()).isEqualTo("avatar17");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateAvatar_ShouldThrowException_WhenAvatarIdIsInvalid() {
        assertThatThrownBy(() -> userService.updateAvatar(1L, "avatar999"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Avatar invalide");

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateAvatar_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateAvatar(999L, "avatar1"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Utilisateur non trouvé avec l'id: 999");
    }
}