package com.alex.cryptoBackend.dto;
import com.alex.cryptoBackend.model.UserState;
import com.alex.cryptoBackend.model.UserStatus;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String username;
    private String lastName;
    private String password;
    private String email;
    private UserStatus status;
    private UserState state;
    private Set<RoleDto> roles = new HashSet<>();
    private Set<WalletDto> wallets = new HashSet<>();
}
