package com.example.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioResponse {
    private int id;
    private String assetType;
    private String symbol;
    private int quantity;
}
