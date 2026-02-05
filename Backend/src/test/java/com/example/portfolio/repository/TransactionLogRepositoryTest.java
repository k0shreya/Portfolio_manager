package com.example.portfolio.repository;

import org.junit.jupiter.api.Test;

class TransactionLogRepositoryTest {

    @Test
    void returnsLogsInDescendingOrderByCreatedAt() {
        com.example.portfolio.repository.TransactionLogRepository repository = org.mockito.Mockito.mock(com.example.portfolio.repository.TransactionLogRepository.class);
        com.example.portfolio.beans.TransactionLog tl1 = org.mockito.Mockito.mock(com.example.portfolio.beans.TransactionLog.class);
        com.example.portfolio.beans.TransactionLog tl2 = org.mockito.Mockito.mock(com.example.portfolio.beans.TransactionLog.class);
        com.example.portfolio.beans.TransactionLog tl3 = org.mockito.Mockito.mock(com.example.portfolio.beans.TransactionLog.class);
        java.util.List<com.example.portfolio.beans.TransactionLog> list = java.util.Arrays.asList(tl1, tl2, tl3);

        org.mockito.Mockito.when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(list);

        java.util.List<com.example.portfolio.beans.TransactionLog> result = repository.findAllByOrderByCreatedAtDesc();

        org.junit.jupiter.api.Assertions.assertEquals(3, result.size());
        org.junit.jupiter.api.Assertions.assertSame(tl1, result.get(0));
        org.junit.jupiter.api.Assertions.assertSame(tl2, result.get(1));
        org.junit.jupiter.api.Assertions.assertSame(tl3, result.get(2));
        org.mockito.Mockito.verify(repository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void returnsEmptyListWhenNoTransactionLogs() {
        com.example.portfolio.repository.TransactionLogRepository repository = org.mockito.Mockito.mock(com.example.portfolio.repository.TransactionLogRepository.class);

        org.mockito.Mockito.when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(java.util.Collections.emptyList());

        java.util.List<com.example.portfolio.beans.TransactionLog> result = repository.findAllByOrderByCreatedAtDesc();

        org.junit.jupiter.api.Assertions.assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertTrue(result.isEmpty());
        org.mockito.Mockito.verify(repository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void preservesSingleTransactionLogInReturnedList() {
        com.example.portfolio.repository.TransactionLogRepository repository = org.mockito.Mockito.mock(com.example.portfolio.repository.TransactionLogRepository.class);
        com.example.portfolio.beans.TransactionLog single = org.mockito.Mockito.mock(com.example.portfolio.beans.TransactionLog.class);
        java.util.List<com.example.portfolio.beans.TransactionLog> list = java.util.Collections.singletonList(single);

        org.mockito.Mockito.when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(list);

        java.util.List<com.example.portfolio.beans.TransactionLog> result = repository.findAllByOrderByCreatedAtDesc();

        org.junit.jupiter.api.Assertions.assertEquals(1, result.size());
        org.junit.jupiter.api.Assertions.assertSame(single, result.get(0));
        org.mockito.Mockito.verify(repository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void handlesNullEntriesWithinReturnedList() {
        com.example.portfolio.repository.TransactionLogRepository repository = org.mockito.Mockito.mock(com.example.portfolio.repository.TransactionLogRepository.class);
        com.example.portfolio.beans.TransactionLog present = org.mockito.Mockito.mock(com.example.portfolio.beans.TransactionLog.class);
        java.util.List<com.example.portfolio.beans.TransactionLog> list = new java.util.ArrayList<>();
        list.add(present);
        list.add(null);
        list.add(present);
        org.mockito.Mockito.when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(list);

        java.util.List<com.example.portfolio.beans.TransactionLog> result = repository.findAllByOrderByCreatedAtDesc();

        org.junit.jupiter.api.Assertions.assertEquals(3, result.size());
        org.junit.jupiter.api.Assertions.assertSame(present, result.get(0));
        org.junit.jupiter.api.Assertions.assertNull(result.get(1));
        org.junit.jupiter.api.Assertions.assertSame(present, result.get(2));
        org.mockito.Mockito.verify(repository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void returnsDifferentResultsAcrossMultipleCalls() {
        com.example.portfolio.repository.TransactionLogRepository repository = org.mockito.Mockito.mock(com.example.portfolio.repository.TransactionLogRepository.class);
        com.example.portfolio.beans.TransactionLog a = org.mockito.Mockito.mock(com.example.portfolio.beans.TransactionLog.class);
        com.example.portfolio.beans.TransactionLog b = org.mockito.Mockito.mock(com.example.portfolio.beans.TransactionLog.class);
        java.util.List<com.example.portfolio.beans.TransactionLog> first = java.util.Collections.singletonList(a);
        java.util.List<com.example.portfolio.beans.TransactionLog> second = java.util.Collections.singletonList(b);

        org.mockito.Mockito.when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(first, second);

        java.util.List<com.example.portfolio.beans.TransactionLog> result1 = repository.findAllByOrderByCreatedAtDesc();
        java.util.List<com.example.portfolio.beans.TransactionLog> result2 = repository.findAllByOrderByCreatedAtDesc();

        org.junit.jupiter.api.Assertions.assertEquals(1, result1.size());
        org.junit.jupiter.api.Assertions.assertSame(a, result1.get(0));
        org.junit.jupiter.api.Assertions.assertEquals(1, result2.size());
        org.junit.jupiter.api.Assertions.assertSame(b, result2.get(0));
        org.mockito.Mockito.verify(repository, org.mockito.Mockito.times(2)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void invocationCountMatchesNumberOfCalls() {
        com.example.portfolio.repository.TransactionLogRepository repository = org.mockito.Mockito.mock(com.example.portfolio.repository.TransactionLogRepository.class);
        org.mockito.Mockito.when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(java.util.Collections.emptyList());

        repository.findAllByOrderByCreatedAtDesc();
        repository.findAllByOrderByCreatedAtDesc();
        repository.findAllByOrderByCreatedAtDesc();

        org.mockito.Mockito.verify(repository, org.mockito.Mockito.times(3)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void handlesLargeNumberOfTransactionLogs() {
        com.example.portfolio.repository.TransactionLogRepository repository = org.mockito.Mockito.mock(com.example.portfolio.repository.TransactionLogRepository.class);
        java.util.List<com.example.portfolio.beans.TransactionLog> large = new java.util.ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            large.add(org.mockito.Mockito.mock(com.example.portfolio.beans.TransactionLog.class));
        }
        org.mockito.Mockito.when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(large);

        java.util.List<com.example.portfolio.beans.TransactionLog> result = repository.findAllByOrderByCreatedAtDesc();

        org.junit.jupiter.api.Assertions.assertEquals(1000, result.size());
        org.mockito.Mockito.verify(repository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void differentRepositoryMocksReturnTheirOwnListsIndependently() {
        com.example.portfolio.repository.TransactionLogRepository repoA = org.mockito.Mockito.mock(com.example.portfolio.repository.TransactionLogRepository.class);
        com.example.portfolio.repository.TransactionLogRepository repoB = org.mockito.Mockito.mock(com.example.portfolio.repository.TransactionLogRepository.class);
        com.example.portfolio.beans.TransactionLog a = org.mockito.Mockito.mock(com.example.portfolio.beans.TransactionLog.class);
        com.example.portfolio.beans.TransactionLog b = org.mockito.Mockito.mock(com.example.portfolio.beans.TransactionLog.class);
        org.mockito.Mockito.when(repoA.findAllByOrderByCreatedAtDesc()).thenReturn(java.util.Collections.singletonList(a));
        org.mockito.Mockito.when(repoB.findAllByOrderByCreatedAtDesc()).thenReturn(java.util.Collections.singletonList(b));

        java.util.List<com.example.portfolio.beans.TransactionLog> resultA = repoA.findAllByOrderByCreatedAtDesc();
        java.util.List<com.example.portfolio.beans.TransactionLog> resultB = repoB.findAllByOrderByCreatedAtDesc();

        org.junit.jupiter.api.Assertions.assertEquals(1, resultA.size());
        org.junit.jupiter.api.Assertions.assertSame(a, resultA.get(0));
        org.junit.jupiter.api.Assertions.assertEquals(1, resultB.size());
        org.junit.jupiter.api.Assertions.assertSame(b, resultB.get(0));
        org.mockito.Mockito.verify(repoA).findAllByOrderByCreatedAtDesc();
        org.mockito.Mockito.verify(repoB).findAllByOrderByCreatedAtDesc();
    }
}
