package com.alex.cryptoBackend.service.impl;

import com.alex.cryptoBackend.dto.*;
import com.alex.cryptoBackend.mapper.MapMapper;
import com.alex.cryptoBackend.model.Role;
import com.alex.cryptoBackend.model.User;
import com.alex.cryptoBackend.model.UserState;
import com.alex.cryptoBackend.repository.RoleRepository;
import com.alex.cryptoBackend.repository.TransactionRepository;
import com.alex.cryptoBackend.repository.UserRepository;
import com.alex.cryptoBackend.repository.WalletRepository;
import com.alex.cryptoBackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final MapMapper mapper;
    private  final PasswordEncoder encoder;

    private final static String USER_EXCEPTION_MESSAGE = "User doesn't exist";
    private final static String ROLE_EXCEPTION_MESSAGE = "Role doesn't exist";

    @Override
    public List<UserDto> getAllUsers() {
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
        for (RoleDto role : newUser.getRoles()) {
            Role role1 = roleRepository.findByName(role.getName()).orElseThrow(() -> new IllegalArgumentException(ROLE_EXCEPTION_MESSAGE));
            roles.add(role1);
        }
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
}
