package com.alex.cryptoBackend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserConfirmationInfo {
    private String fullName;
    private String username;
    private String token;
    private String email;
}
