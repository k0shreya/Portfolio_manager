// java
package com.example.portfolio.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DashboardResponseTest {

    @Test
    void allowsNullAssets() {
        DashboardResponse dr = DashboardResponse.builder()
                .cashBalance(0.0)
                .totalAssetValue(0.0)
                .totalPortfolioValue(0.0)
                .assets(null)
                .build();

        assertNull(dr.getAssets());
    }

    @Test
    void acceptsEmptyAssetsList() {
        DashboardResponse dr = new DashboardResponse();
        dr.setAssets(List.of());
        assertNotNull(dr.getAssets());
        assertEquals(0, dr.getAssets().size());
    }

    @Test
    void assetSummaryNoArgsDefaults() {
        DashboardResponse.AssetSummary s = new DashboardResponse.AssetSummary();
        assertNull(s.getSymbol());
        assertNull(s.getAssetType());
        assertEquals(0, s.getQuantity());
        assertEquals(0.0, s.getAvgBuyPrice());
        assertEquals(0.0, s.getCurrentPrice());
        assertEquals(0.0, s.getInvestedValue());
        assertEquals(0.0, s.getCurrentValue());
        assertEquals(0.0, s.getProfitOrLoss());
    }

    @Test
    void assetSummarySettersOverwrite() {
        DashboardResponse.AssetSummary s = new DashboardResponse.AssetSummary();
        s.setSymbol("TSLA");
        s.setAssetType("STOCK");
        s.setQuantity(3);
        s.setAvgBuyPrice(100.5);
        s.setCurrentPrice(110.75);
        s.setInvestedValue(301.5);
        s.setCurrentValue(332.25);
        s.setProfitOrLoss(30.75);

        assertEquals("TSLA", s.getSymbol());
        assertEquals("STOCK", s.getAssetType());
        assertEquals(3, s.getQuantity());
        assertEquals(100.5, s.getAvgBuyPrice());
        assertEquals(110.75, s.getCurrentPrice());
        assertEquals(301.5, s.getInvestedValue());
        assertEquals(332.25, s.getCurrentValue());
        assertEquals(30.75, s.getProfitOrLoss());
    }

    @Test
    void assetsListImmutableWhenUsingListOf() {
        DashboardResponse.AssetSummary s = DashboardResponse.AssetSummary.builder()
                .symbol("A")
                .assetType("STOCK")
                .quantity(1)
                .build();

        DashboardResponse dr = DashboardResponse.builder()
                .assets(List.of(s))
                .build();

        assertThrows(UnsupportedOperationException.class, () -> dr.getAssets().add(new DashboardResponse.AssetSummary()));
    }

    @Test
    void assetSummariesAreIndependent() {
        DashboardResponse.AssetSummary a = DashboardResponse.AssetSummary.builder()
                .symbol("X")
                .assetType("STOCK")
                .quantity(1)
                .build();

        DashboardResponse.AssetSummary b = DashboardResponse.AssetSummary.builder()
                .symbol("Y")
                .assetType("BOND")
                .quantity(2)
                .build();

        a.setSymbol("X-modified");
        a.setQuantity(99);

        assertEquals("X-modified", a.getSymbol());
        assertEquals(99, a.getQuantity());

        // ensure b remains unchanged
        assertEquals("Y", b.getSymbol());
        assertEquals(2, b.getQuantity());
    }

    @Test
    void highPrecisionValuesPreservedInAssetSummary() {
        double precise = 12345.678901234567;
        DashboardResponse.AssetSummary s = DashboardResponse.AssetSummary.builder()
                .avgBuyPrice(precise)
                .build();

        assertEquals(precise, s.getAvgBuyPrice(), 1e-12);
    }
}
