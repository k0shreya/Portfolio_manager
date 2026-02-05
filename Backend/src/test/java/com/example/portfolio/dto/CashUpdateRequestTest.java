// java
package com.example.portfolio.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CashUpdateRequestTest {

    @Test
    void testCashUpdateRequestUsingBuilder() {
        // Given
        double amount = 1000.75;

        // When
        CashUpdateRequest cashUpdateRequest = CashUpdateRequest.builder()
                .amount(amount)
                .build();

        // Then
        assertEquals(amount, cashUpdateRequest.getAmount());
    }

    @Test
    void testCashUpdateRequestConstructor() {
        // Given
        double amount = 500.50;

        // When
        CashUpdateRequest cashUpdateRequest = new CashUpdateRequest(amount);

        // Then
        assertEquals(amount, cashUpdateRequest.getAmount());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        CashUpdateRequest cashUpdateRequest = new CashUpdateRequest();

        // When
        cashUpdateRequest.setAmount(2000.25);

        // Then
        assertEquals(2000.25, cashUpdateRequest.getAmount());
    }

    @Test
    void builderDefaultsToZeroWhenNotSet() {
        // When
        CashUpdateRequest cashUpdateRequest = CashUpdateRequest.builder().build();

        // Then
        assertEquals(0.0, cashUpdateRequest.getAmount());
    }

    @Test
    void allowsZeroAmount() {
        // Given
        double amount = 0.0;

        // When
        CashUpdateRequest req = CashUpdateRequest.builder().amount(amount).build();

        // Then
        assertEquals(0.0, req.getAmount());
    }

    @Test
    void allowsNegativeAmount() {
        // Given
        double amount = -123.45;

        // When
        CashUpdateRequest req = new CashUpdateRequest(amount);

        // Then
        assertEquals(-123.45, req.getAmount());
    }

    @Test
    void preservesHighPrecisionAmount() {
        // Given
        double amount = 12345678.901234567;

        // When
        CashUpdateRequest req = CashUpdateRequest.builder().amount(amount).build();

        // Then (allow tiny epsilon)
        assertEquals(amount, req.getAmount(), 1e-9);
    }

    @Test
    void instancesAreIndependentWhenModified() {
        // Given
        CashUpdateRequest a = CashUpdateRequest.builder().amount(10.0).build();
        CashUpdateRequest b = CashUpdateRequest.builder().amount(20.0).build();

        // When
        a.setAmount(99.99);

        // Then
        assertEquals(99.99, a.getAmount(), 1e-9);
        assertEquals(20.0, b.getAmount(), 1e-9);
    }

    @Test
    void twoSeparateInstancesWithSameValueAreNotSameObject() {
        // Given
        double value = 42.42;
        CashUpdateRequest a = new CashUpdateRequest(value);
        CashUpdateRequest b = new CashUpdateRequest(value);

        // Then
        assertEquals(a.getAmount(), b.getAmount(), 1e-9);
        assertNotSame(a, b);
    }
}
