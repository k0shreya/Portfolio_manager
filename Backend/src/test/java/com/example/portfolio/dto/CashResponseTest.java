// language: java
package com.example.portfolio.dto;

import com.example.portfolio.dto.CashResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CashResponseTest {

    @Test
    void testCashResponseUsingBuilder() {
        double cashBalance = 1000.50;
        CashResponse cashResponse = CashResponse.builder()
                .cashBalance(cashBalance)
                .build();
        assertEquals(cashBalance, cashResponse.getCashBalance());
    }

    @Test
    void testCashResponseConstructor() {
        double cashBalance = 500.75;
        CashResponse cashResponse = new CashResponse(cashBalance);
        assertEquals(cashBalance, cashResponse.getCashBalance());
    }

    @Test
    void testSettersAndGetters() {
        CashResponse cashResponse = new CashResponse();
        cashResponse.setCashBalance(2000.25);
        assertEquals(2000.25, cashResponse.getCashBalance());
    }

    @Test
    void testDefaultNoArgsConstructorInitializesToZero() {
        CashResponse cashResponse = new CashResponse();
        assertEquals(0.0, cashResponse.getCashBalance());
    }

    @Test
    void testSetterOverwritesPreviousValue() {
        CashResponse cashResponse = CashResponse.builder().cashBalance(150.0).build();
        assertEquals(150.0, cashResponse.getCashBalance());
        cashResponse.setCashBalance(300.5);
        assertEquals(300.5, cashResponse.getCashBalance());
    }

    @Test
    void testHighPrecisionValuePreserved() {
        double highPrecision = 12345.678901234;
        CashResponse cashResponse = CashResponse.builder().cashBalance(highPrecision).build();
        assertEquals(highPrecision, cashResponse.getCashBalance());
    }

    @Test
    void testNegativeCashBalanceAllowed() {
        double negative = -999.99;
        CashResponse cashResponse = new CashResponse(negative);
        assertEquals(negative, cashResponse.getCashBalance());
    }

    @Test
    void testInstancesAreIndependentWhenModified() {
        CashResponse a = CashResponse.builder().cashBalance(10.0).build();
        CashResponse b = CashResponse.builder().cashBalance(20.0).build();
        a.setCashBalance(99.99);
        assertEquals(99.99, a.getCashBalance());
        assertEquals(20.0, b.getCashBalance());
    }

    @Test
    void testSeparateInstancesWithSameValueAreNotSameObject() {
        double value = 42.42;
        CashResponse a = new CashResponse(value);
        CashResponse b = new CashResponse(value);
        assertEquals(a.getCashBalance(), b.getCashBalance());
        assertNotSame(a, b);
        assertNotNull(a);
        assertNotNull(b);
    }
}
