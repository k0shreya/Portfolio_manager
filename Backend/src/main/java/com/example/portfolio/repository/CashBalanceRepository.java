package com.example.portfolio.repository;

import com.example.portfolio.beans.CashBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashBalanceRepository
        extends JpaRepository<CashBalance, Integer> {
}
