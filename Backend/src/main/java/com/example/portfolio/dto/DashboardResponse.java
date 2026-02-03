package com.example.portfolio.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private double cashBalance;
    private double totalAssetValue;
    private double totalPortfolioValue;
    private List<AssetSummary> assets;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AssetSummary {

        private String symbol;
        private String assetType;
        private int quantity;

        private double avgBuyPrice;
        private double currentPrice;

        private double investedValue;
        private double currentValue;
        private double profitOrLoss;
    }
}
