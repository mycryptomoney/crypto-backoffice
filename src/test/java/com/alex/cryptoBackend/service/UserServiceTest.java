package com.alex.cryptoBackend.service;

import com.alex.cryptoBackend.dto.NewUserDto;
import com.alex.cryptoBackend.dto.UserDto;
import com.alex.cryptoBackend.mapper.MapMapper;
import com.alex.cryptoBackend.model.ERole;
import com.alex.cryptoBackend.model.Role;
import com.alex.cryptoBackend.model.User;
import com.alex.cryptoBackend.model.UserState;
import com.alex.cryptoBackend.repository.RoleRepository;
import com.alex.cryptoBackend.repository.TransactionRepository;
import com.alex.cryptoBackend.repository.UserRepository;
import com.alex.cryptoBackend.repository.WalletRepository;
import com.alex.cryptoBackend.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private  WalletRepository walletRepository;
    @MockBean
    private MapMapper mapper;
    @MockBean
    private ConfirmationTokenService confirmationTokenService;
    @MockBean
    private  PasswordEncoder encoder;
    @MockBean
    private EmailService emailService;
    private Long expireMinutes = 20L;

    private final UserDto userDto1 = new UserDto();
    private final UserDto userDto2 = new UserDto();
    private final User user1 = new User();
    private final User user2 = new User();
    private final User user3 = new User();
    private final Role user = new Role();
    private final Role admin = new Role();
    private List<User> allUsers;
    private List<UserDto> allUserDtos;
    private List<User> allActiveUsers;
    private final String USER_EXCEPTION_MESSAGE = "User doesn't exist";

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, roleRepository, mapper, encoder, confirmationTokenService, emailService, expireMinutes);
        userDto1.setEmail("Jonny@gmail.com");
        userDto1.setId(1L);
        userDto1.setUsername("Username");
        userDto1.setState(UserState.ACTIVE);
        userDto2.setEmail("Jonny1@gmail.com");
        userDto2.setUsername("Username1");
        userDto2.setId(2L);
        userDto2.setState(UserState.ACTIVE);
        user1.setEmail("Jonny@gmail.com");
        user1.setUsername("Username");
        user1.setState(UserState.ACTIVE);
        user1.getRoles().add(admin);
        user1.setId(1L);
        user2.setEmail("Jonny1@gmail.com");
        user2.setUsername("Username1");
        user2.setState(UserState.ACTIVE);
        user2.setId(2L);
        user2.getRoles().add(user);
        user3.getRoles().add(admin);
        user3.setEmail("Jonny12@gmail.com");
        user3.setUsername("Username1");
        user3.setState(UserState.DELETED);
        user3.setId(3L);
        allUsers = List.of(user1, user2, user3);
        allUserDtos = List.of(userDto1, userDto2);
        allActiveUsers = List.of(user1, user2);
    }

    @Test
    void testGetAllUsers() {
        final int actualSize = 2;
        when(userRepository.findAll()).thenReturn(allUsers);
        when(mapper.toDto(anyList())).thenReturn(allUserDtos);
        List<UserDto> actualUserDtos = userService.getAllActiveUsers();
        System.out.println(actualUserDtos.get(0));
        System.out.println(allUsers.get(0));
        verify(mapper).toDto(eq(allActiveUsers));
        verify(userRepository).findAll();
        assertThat(actualUserDtos).isNotEmpty().isEqualTo(allUserDtos);
        assertThat(actualUserDtos.size()).isEqualTo(actualSize);
    }

    @Test
    void testUserGetByIdHappyPath() {
        final long actual = 1;
        when(userRepository.findById(actual)).thenReturn(java.util.Optional.of(user1));
        when(mapper.toDto(eq(user1))).thenReturn(userDto1);
        UserDto userDto = userService.getUserById(actual);
        assertThat(userDto).isEqualTo(userDto1);
    }

    @Test
    void testUserGetByIdUnhappyPathWrongId() {
        final long nonActual = 100;
        when(userRepository.findById(nonActual)).thenReturn(Optional.empty());
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->  userService.getUserById(nonActual))
                .withMessage(USER_EXCEPTION_MESSAGE);
    }

    @Test
    void testUserGetByIdUnhappyPathDeletedUser() {
        final long actual = 3;
        when(userRepository.findById(actual)).thenReturn(Optional.of(user3));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->  userService.getUserById(actual))
                .withMessage(USER_EXCEPTION_MESSAGE);
    }

    @Test
    void testDeleteUserHappyPath() {
        final long actual = 1;
        when(userRepository.findById(actual)).thenReturn(java.util.Optional.of(user1));
        when(userRepository.save(eq(user1))).thenReturn(user1);
        userService.deleteUserById(actual);
        assertThat(user1.getState()).isEqualTo(UserState.DELETED);
    }

    @Test
    void testDeleteUserUnhappyPath() {
        final long nonActual = 100;
        when(userRepository.findById(nonActual)).thenReturn(Optional.empty());
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->  userService.deleteUserById(nonActual))
                .withMessage(USER_EXCEPTION_MESSAGE);
    }

    @Test
    void testCreateUser() {
        final String userEmail = "Test@gmail.com";
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setEmail(userEmail);
        User newUser = new User();
        newUser.getRoles().add(user);
        newUser.setEmail(userEmail);
        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setEmail(userEmail);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(admin));
        when(mapper.toUser(eq(newUserDto))).thenReturn(newUser);
        when(mapper.toDto(eq(newUser))).thenReturn(expectedUserDto);
        UserDto actualUserDto = userService.createUser(newUserDto);
        verify(userRepository).save(eq(newUser));
        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

    @Test
    void testUpdateUserHappyPath() {
        final long id = 1;
        final String userUsername = "newUsername";
        final String userEmail = "userEmail";
        userDto1.setUsername(userUsername);
        userDto1.setEmail(userEmail);
        User updatedUser = user1;
        updatedUser.setUsername(userUsername);
        updatedUser.setEmail(userEmail);
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        when(mapper.toDto(eq(updatedUser))).thenReturn(userDto1);
        when(mapper.toUser(eq(userDto1))).thenReturn(updatedUser);
        UserDto actualUserDto = userService.updateUser(userDto1, id);
        assertThat(actualUserDto).isEqualTo(userDto1);
    }

    @Test
    void testUpdateUserUnhappyPath() {
        final long nonActual = 100;
        when(userRepository.findById(nonActual)).thenReturn(Optional.empty());
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->  userService.updateUser(userDto1, nonActual))
                .withMessage(USER_EXCEPTION_MESSAGE);
    }

}
