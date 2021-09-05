package com.alex.cryptoBackend.dto;

import com.alex.cryptoBackend.model.ERole;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoleDto {
    @NotNull(message = "Role can't be null")
    private ERole name;
}
