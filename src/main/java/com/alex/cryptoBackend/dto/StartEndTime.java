package com.alex.cryptoBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartEndTime {
    private LocalDate startDate;
    private LocalDate endDate;
}
