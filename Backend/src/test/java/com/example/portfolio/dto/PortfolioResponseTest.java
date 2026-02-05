// java
package com.example.portfolio.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioResponseTest {

    @Test
    void noArgsConstructorDefaults() {
        PortfolioResponse resp = new PortfolioResponse();
        assertEquals(0, resp.getId());
        assertNull(resp.getAssetType());
        assertNull(resp.getSymbol());
        assertEquals(0, resp.getQuantity());
    }

    @Test
    void allowsZeroAndNegativeQuantityInAllArgsConstructor() {
        PortfolioResponse zero = new PortfolioResponse(1, "STOCK", "AAPL", 0);
        PortfolioResponse negative = new PortfolioResponse(2, "BOND", "TBOND", -10);

        assertEquals(0, zero.getQuantity());
        assertEquals(-10, negative.getQuantity());
        assertEquals("STOCK", zero.getAssetType());
        assertEquals("BOND", negative.getAssetType());
    }

    @Test
    void settersOverwriteAndInstancesIndependent() {
        PortfolioResponse a = new PortfolioResponse();
        PortfolioResponse b = new PortfolioResponse();

        a.setAssetType("REAL_ESTATE");
        a.setSymbol("REIT");
        a.setQuantity(5);

        // overwrite a
        a.setAssetType("STOCK");
        a.setSymbol("AAPL");
        a.setQuantity(100);

        // set b independently
        b.setAssetType("BOND");
        b.setSymbol("GOV");
        b.setQuantity(2);

        assertEquals("STOCK", a.getAssetType());
        assertEquals("AAPL", a.getSymbol());
        assertEquals(100, a.getQuantity());

        assertEquals("BOND", b.getAssetType());
        assertEquals("GOV", b.getSymbol());
        assertEquals(2, b.getQuantity());
    }

    @Test
    void equalInstancesHaveSameHashCodeButAreDifferentObjects() {
        PortfolioResponse a = new PortfolioResponse(10, "STOCK", "XYZ", 50);
        PortfolioResponse b = new PortfolioResponse(10, "STOCK", "XYZ", 50);

        assertEquals(a, b);
        assertNotSame(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toStringContainsKeyFields() {
        PortfolioResponse resp = new PortfolioResponse(3, "STOCK", "MSFT", 20);
        String s = resp.toString();
        assertNotNull(s);
        assertTrue(s.contains("assetType=STOCK"));
        assertTrue(s.contains("symbol=MSFT"));
        assertTrue(s.contains("quantity=20"));
    }
}
