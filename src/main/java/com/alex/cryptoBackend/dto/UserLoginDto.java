package com.alex.cryptoBackend.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class UserLoginDto {
    @NotNull(message = "Username can't be null")
    private String username;
    @NotNull(message = "Password can't be null")
    private String password;
}
