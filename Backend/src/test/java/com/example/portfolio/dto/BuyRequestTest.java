// java
package com.example.portfolio.dto;

import com.example.portfolio.beans.AssetType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuyRequestTest {

    @Test
    void testBuyRequestUsingBuilder() {
        AssetType assetType = AssetType.STOCK;
        String symbol = "AAPL";
        int quantity = 10;

        BuyRequest buyRequest = BuyRequest.builder()
                .assetType(assetType)
                .symbol(symbol)
                .quantity(quantity)
                .build();

        assertEquals(assetType, buyRequest.getAssetType());
        assertEquals(symbol, buyRequest.getSymbol());
        assertEquals(quantity, buyRequest.getQuantity());
    }

    @Test
    void testBuyRequestConstructor() {
        AssetType assetType = AssetType.BOND;
        String symbol = "GOOGL";
        int quantity = 5;

        BuyRequest buyRequest = new BuyRequest(assetType, symbol, quantity);

        assertEquals(assetType, buyRequest.getAssetType());
        assertEquals(symbol, buyRequest.getSymbol());
        assertEquals(quantity, buyRequest.getQuantity());
    }

    @Test
    void testSettersAndGetters() {
        BuyRequest buyRequest = new BuyRequest();

        buyRequest.setAssetType(AssetType.REAL_ESTATE);
        buyRequest.setSymbol("TSLA");
        buyRequest.setQuantity(15);

        assertEquals(AssetType.REAL_ESTATE, buyRequest.getAssetType());
        assertEquals("TSLA", buyRequest.getSymbol());
        assertEquals(15, buyRequest.getQuantity());
    }

    @Test
    void builderAllowsZeroQuantity() {
        BuyRequest buyRequest = BuyRequest.builder()
                .assetType(AssetType.STOCK)
                .symbol("MSFT")
                .quantity(0)
                .build();

        assertEquals(0, buyRequest.getQuantity());
        assertEquals("MSFT", buyRequest.getSymbol());
        assertEquals(AssetType.STOCK, buyRequest.getAssetType());
    }

    @Test
    void builderAllowsNegativeQuantity() {
        BuyRequest buyRequest = BuyRequest.builder()
                .assetType(AssetType.BOND)
                .symbol("IBM")
                .quantity(-10)
                .build();

        assertEquals(-10, buyRequest.getQuantity());
        assertEquals("IBM", buyRequest.getSymbol());
        assertEquals(AssetType.BOND, buyRequest.getAssetType());
    }

    @Test
    void builderAllowsNullSymbol() {
        BuyRequest buyRequest = BuyRequest.builder()
                .assetType(AssetType.STOCK)
                .symbol(null)
                .quantity(3)
                .build();

        assertNull(buyRequest.getSymbol());
        assertEquals(3, buyRequest.getQuantity());
        assertEquals(AssetType.STOCK, buyRequest.getAssetType());
    }

    @Test
    void builderAllowsNullAssetType() {
        BuyRequest buyRequest = BuyRequest.builder()
                .assetType(null)
                .symbol("NFLX")
                .quantity(7)
                .build();

        assertNull(buyRequest.getAssetType());
        assertEquals("NFLX", buyRequest.getSymbol());
        assertEquals(7, buyRequest.getQuantity());
    }

    @Test
    void instancesAreIndependentWhenModified() {
        BuyRequest a = BuyRequest.builder().assetType(AssetType.STOCK).symbol("A").quantity(1).build();
        BuyRequest b = BuyRequest.builder().assetType(AssetType.BOND).symbol("B").quantity(2).build();

        a.setSymbol("A-modified");
        a.setQuantity(100);

        assertEquals("A-modified", a.getSymbol());
        assertEquals(100, a.getQuantity());

        // ensure b remains unchanged
        assertEquals("B", b.getSymbol());
        assertEquals(2, b.getQuantity());
        assertEquals(AssetType.BOND, b.getAssetType());
    }

    @Test
    void settersOverwritePreviousValues() {
        BuyRequest buyRequest = new BuyRequest();
        buyRequest.setAssetType(AssetType.REAL_ESTATE);
        buyRequest.setSymbol("OLD");
        buyRequest.setQuantity(1);

        // overwrite
        buyRequest.setAssetType(AssetType.STOCK);
        buyRequest.setSymbol("NEW");
        buyRequest.setQuantity(42);

        assertEquals(AssetType.STOCK, buyRequest.getAssetType());
        assertEquals("NEW", buyRequest.getSymbol());
        assertEquals(42, buyRequest.getQuantity());
    }
}
