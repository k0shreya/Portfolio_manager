package com.example.portfolio.repository;

import com.example.portfolio.beans.CashBalance;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CashBalanceRepositoryTest {

    @Test
    void savesCashBalanceSuccessfully() {
        CashBalanceRepository repository = Mockito.mock(CashBalanceRepository.class);
        CashBalance cash = Mockito.mock(CashBalance.class);

        Mockito.when(repository.save(cash)).thenReturn(cash);

        CashBalance result = repository.save(cash);

        assertSame(cash, result);
        Mockito.verify(repository).save(cash);
    }

    @Test
    void findByIdReturnsCashBalanceWhenPresent() {
        CashBalanceRepository repository = Mockito.mock(CashBalanceRepository.class);
        CashBalance cash = Mockito.mock(CashBalance.class);
        Optional<CashBalance> optional = Optional.of(cash);

        Mockito.when(repository.findById(1)).thenReturn(optional);

        Optional<CashBalance> result = repository.findById(1);

        assertTrue(result.isPresent());
        assertSame(cash, result.get());
        Mockito.verify(repository).findById(1);
    }

    @Test
    void findByIdReturnsEmptyWhenNotFound() {
        CashBalanceRepository repository = Mockito.mock(CashBalanceRepository.class);

        Mockito.when(repository.findById(Integer.valueOf(999))).thenReturn(Optional.empty());

        Optional<CashBalance> result = repository.findById(999);

        assertFalse(result.isPresent());
        Mockito.verify(repository).findById(999);
    }

    @Test
    void findAllReturnsListOfCashBalances() {
        CashBalanceRepository repository = Mockito.mock(CashBalanceRepository.class);
        CashBalance c1 = Mockito.mock(CashBalance.class);
        CashBalance c2 = Mockito.mock(CashBalance.class);
        List<CashBalance> list = Arrays.asList(c1, c2);

        Mockito.when(repository.findAll()).thenReturn(list);

        List<CashBalance> result = repository.findAll();

        assertEquals(2, result.size());
        assertSame(c1, result.get(0));
        assertSame(c2, result.get(1));
        Mockito.verify(repository).findAll();
    }

    @Test
    void deleteByIdInvokedWithCorrectId() {
        CashBalanceRepository repository = Mockito.mock(CashBalanceRepository.class);

        Mockito.doNothing().when(repository).deleteById(42);

        repository.deleteById(42);

        Mockito.verify(repository).deleteById(42);
        Mockito.verifyNoMoreInteractions(repository);
    }
}
