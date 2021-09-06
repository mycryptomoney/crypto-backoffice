package com.alex.cryptoBackend.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Wallet sender;
    @ManyToOne
    private Wallet receiver;
    @Column(nullable = false)
    private LocalDateTime time;
    @Column(nullable = false)
    private BigDecimal amount;
}
