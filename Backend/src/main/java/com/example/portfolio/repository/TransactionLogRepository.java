package com.example.portfolio.repository;

import com.example.portfolio.beans.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionLogRepository
        extends JpaRepository<TransactionLog, Long> {

    List<TransactionLog> findAllByOrderByCreatedAtDesc();
}

