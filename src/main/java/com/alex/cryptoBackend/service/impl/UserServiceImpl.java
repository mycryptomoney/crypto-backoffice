package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.dto.*;
import com.alex.cryptoBackend.mapper.MapMapper;
import com.alex.cryptoBackend.model.*;
import com.alex.cryptoBackend.repository.RoleRepository;
import com.alex.cryptoBackend.repository.UserRepository;
import com.alex.cryptoBackend.service.ConfirmationTokenService;
import com.alex.cryptoBackend.service.EmailService;
import com.alex.cryptoBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MapMapper mapper;
    private final PasswordEncoder encoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    @Value("${token.expire.minutes}")
    private final long expireMinutesLimit;
    private final static String USER_EXCEPTION_MESSAGE = "User doesn't exist";
    private final static String ROLE_EXCEPTION_MESSAGE = "Role doesn't exist";
    private final static String EMAIL_EXIST_EXCEPTION = "Email already exist";
    private final static String USERNAME_EXIST_EXCEPTION = "Username already exist";

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> users = mapper.toDto(userRepository.findAll());
        return users;
    }

    @Override
    public List<UserDto> getAllActiveUsers() {
        List<UserDto> users = mapper.toDto(
                userRepository.findAll().stream()
                        .filter(user -> user.getState() == UserState.ACTIVE)
                        .collect(Collectors.toList())
        );
        return users;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).filter(thisUser -> thisUser.getState() == UserState.ACTIVE).orElseThrow(() -> new IllegalArgumentException(USER_EXCEPTION_MESSAGE));
        return mapper.toDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(USER_EXCEPTION_MESSAGE));
        user.setState(UserState.DELETED);
        userRepository.save(user);
    }

    @Override
    public UserDto createUser(NewUserDto newUser) {
        User user = mapper.toUser(newUser);
        Set<Role> roles = new HashSet<>();
        Role role1 = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new IllegalArgumentException(ROLE_EXCEPTION_MESSAGE));
        roles.add(role1);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);
        return mapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto user, Long id) {
        userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(USER_EXCEPTION_MESSAGE));
        user.setId(id);
        User updatedUser = mapper.toUser(user);
        userRepository.save(updatedUser);
        return mapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public UserDto createInitialUser(NewUserDto registerRequest) {
        boolean usernameExist = userRepository.findByUsername(registerRequest.getUsername()).isPresent();
        boolean emailExist = userRepository.findByEmail(registerRequest.getEmail()).isPresent();
        if (emailExist) {
            throw new IllegalArgumentException(EMAIL_EXIST_EXCEPTION);
        }
        if (usernameExist) {
            throw new IllegalArgumentException(USERNAME_EXIST_EXCEPTION);
        }
        User newUser = mapper.toUser(registerRequest);
        String encodedPassword = encoder.encode(registerRequest.getPassword());
        newUser.setPassword(encodedPassword);
        Role role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new IllegalArgumentException(ROLE_EXCEPTION_MESSAGE));
        newUser.getRoles().add(role);
        newUser.setState(UserState.FROZEN);
        userRepository.save(newUser);
        final String token = UUID.randomUUID().toString();
        final ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(expireMinutesLimit))
                .user(newUser)
                .build();
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        emailService.send(confirmationToken);
        return mapper.toDto(newUser);
    }

    @Override
    public Boolean isUserFrozen(String username) {
        User user = userRepository.findByUsernameOrEmail(username).orElseThrow(() -> new IllegalArgumentException(USER_EXCEPTION_MESSAGE));
        return user.getState() == UserState.FROZEN;
    }

    @Override
    public void enableUser(String email) {
        userRepository.enableUser(email);
    }
}
