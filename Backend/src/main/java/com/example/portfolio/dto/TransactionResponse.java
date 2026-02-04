package com.example.portfolio.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private String transactionId;
    private String symbol;
    private String assetType;
    private int quantity;
    private double amount;
    private String transactionType;
    private LocalDateTime createdAt;
}
