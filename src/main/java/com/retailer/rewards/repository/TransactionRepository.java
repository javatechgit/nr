package com.retailer.rewards.repository;

import com.retailer.rewards.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(Long customerId);

    @Query("SELECT t FROM Transaction t WHERE t.customer.customerId = :customerId " +
           "AND t.transactionDate >= :startDate AND t.transactionDate < :endDate")
    List<Transaction> findTransactionsByCustomerIdAndDateRange(
            @Param("customerId") String customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate >= :startDate " +
           "AND t.transactionDate < :endDate ORDER BY t.customer.customerId, t.transactionDate")
    List<Transaction> findAllTransactionsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}