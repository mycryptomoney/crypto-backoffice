package com.alex.cryptoBackend.service;

import com.alex.cryptoBackend.dto.*;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    void deleteUserById(Long id);
    UserDto createUser(NewUserDto newUser);
    UserDto updateUser(UserDto user, Long id);
}
