// java
package com.example.portfolio.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioRequestTest {

    @Test
    void noArgsConstructorDefaults() {
        PortfolioRequest req = new PortfolioRequest();
        assertNull(req.getAssetType());
        assertNull(req.getSymbol());
        assertEquals(0, req.getQuantity());
    }

    @Test
    void constructorAllowsNulls() {
        PortfolioRequest req = new PortfolioRequest(null, null, 3);
        assertNull(req.getAssetType());
        assertNull(req.getSymbol());
        assertEquals(3, req.getQuantity());
    }

    @Test
    void allowsZeroAndNegativeQuantity() {
        PortfolioRequest zero = new PortfolioRequest("STOCK", "AAPL", 0);
        PortfolioRequest negative = new PortfolioRequest("BOND", "TBOND", -5);

        assertEquals(0, zero.getQuantity());
        assertEquals(-5, negative.getQuantity());
        assertEquals("STOCK", zero.getAssetType());
        assertEquals("BOND", negative.getAssetType());
    }

    @Test
    void settersOverwritePreviousValues() {
        PortfolioRequest req = new PortfolioRequest();
        req.setAssetType("REAL_ESTATE");
        req.setSymbol("REIT");
        req.setQuantity(1);

        // overwrite
        req.setAssetType("STOCK");
        req.setSymbol("AAPL");
        req.setQuantity(100);

        assertEquals("STOCK", req.getAssetType());
        assertEquals("AAPL", req.getSymbol());
        assertEquals(100, req.getQuantity());
    }

    @Test
    void instancesAreIndependentWhenModified() {
        PortfolioRequest a = new PortfolioRequest("STOCK", "A", 1);
        PortfolioRequest b = new PortfolioRequest("BOND", "B", 2);

        a.setSymbol("A-mod");
        a.setQuantity(999);

        assertEquals("A-mod", a.getSymbol());
        assertEquals(999, a.getQuantity());

        // ensure b remains unchanged
        assertEquals("B", b.getSymbol());
        assertEquals(2, b.getQuantity());
        assertEquals("BOND", b.getAssetType());
    }

    @Test
    void equalInstancesHaveSameHashCodeButAreDifferentObjects() {
        PortfolioRequest a = new PortfolioRequest("STOCK", "XYZ", 10);
        PortfolioRequest b = new PortfolioRequest("STOCK", "XYZ", 10);

        assertEquals(a, b);
        assertNotSame(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
