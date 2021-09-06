package com.alex.cryptoBackend.dto;

import com.alex.cryptoBackend.model.UserStatus;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewUserDto {
    @NotNull(message = "Firstname can't be null")
    private String firstName;
    @NotNull(message = "Lastname can't be null")
    private String lastName;
    @NotNull(message = "Username can't be null")
    private String username;
    @Size(min = 7, message = "Password has to be no smaller than 7 characters")
    private String password;
    @Email(message = "Email need to be in email format")
    @NotNull(message = "Email can't be null")
    private String email;
    @NotNull(message = "User state can't be null")
    private UserStatus status;
}
