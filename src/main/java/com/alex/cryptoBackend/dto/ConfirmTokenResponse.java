package com.alex.cryptoBackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmTokenResponse {
    private String response;
    private String username;
}
