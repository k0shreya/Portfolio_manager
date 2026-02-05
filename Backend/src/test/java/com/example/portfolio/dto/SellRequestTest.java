// java
package com.example.portfolio.dto;

import com.example.portfolio.beans.AssetType;
import com.example.portfolio.dto.SellRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SellRequestTest {

    @Test
    void builderDefaultsToNullAndZero() {
        SellRequest req = SellRequest.builder().build();
        assertNull(req.getAssetType());
        assertNull(req.getSymbol());
        assertEquals(0, req.getQuantity());
    }

    @Test
    void allowsNullAssetTypeAndEmptySymbol() {
        SellRequest req = new SellRequest(null, "", 0);
        assertNull(req.getAssetType());
        assertEquals("", req.getSymbol());
        assertEquals(0, req.getQuantity());
    }

    @Test
    void allowsZeroAndNegativeQuantity() {
        SellRequest zero = new SellRequest(AssetType.STOCK, "AAPL", 0);
        SellRequest negative = new SellRequest(AssetType.BOND, "TBOND", -5);

        assertEquals(0, zero.getQuantity());
        assertEquals(-5, negative.getQuantity());
    }

    @Test
    void settersOverwritePreviousValues() {
        SellRequest req = new SellRequest();
        req.setAssetType(AssetType.REAL_ESTATE);
        req.setSymbol("PROP");
        req.setQuantity(1);

        // overwrite
        req.setAssetType(AssetType.STOCK);
        req.setSymbol("AAPL");
        req.setQuantity(100);

        assertEquals(AssetType.STOCK, req.getAssetType());
        assertEquals("AAPL", req.getSymbol());
        assertEquals(100, req.getQuantity());
    }

    @Test
    void instancesAreIndependentWhenModified() {
        SellRequest a = new SellRequest(AssetType.STOCK, "X", 1);
        SellRequest b = new SellRequest(AssetType.BOND, "Y", 2);

        a.setSymbol("X-mod");
        a.setQuantity(999);

        assertEquals("X-mod", a.getSymbol());
        assertEquals(999, a.getQuantity());

        // ensure b remains unchanged
        assertEquals("Y", b.getSymbol());
        assertEquals(2, b.getQuantity());
        assertEquals(AssetType.BOND, b.getAssetType());
    }

    @Test
    void largeQuantityPreserved() {
        int large = Integer.MAX_VALUE;
        SellRequest req = SellRequest.builder()
                .assetType(AssetType.STOCK)
                .symbol("BIG")
                .quantity(large)
                .build();

        assertEquals(large, req.getQuantity());
    }

    @Test
    void twoSeparateInstancesWithSameValuesAreNotSameObject() {
        SellRequest a = new SellRequest(AssetType.STOCK, "SAME", 10);
        SellRequest b = new SellRequest(AssetType.STOCK, "SAME", 10);

        assertEquals(a.getAssetType(), b.getAssetType());
        assertEquals(a.getSymbol(), b.getSymbol());
        assertEquals(a.getQuantity(), b.getQuantity());
        assertNotSame(a, b);
    }
}
