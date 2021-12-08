package com.alex.cryptoBackend.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String abbreviation;
    @Column(nullable = false)
    private BigDecimal value;
    @Column(nullable = false)
    private Boolean activated;
    @Column(nullable = false)
    private BigDecimal capitalisation;
    @Lob
    private Byte[] image;
}
