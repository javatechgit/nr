package com.retailer.rewards.service;

import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.RewardPoints;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardsService {

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Calculate reward points for all customers over a date range
     */
    public List<RewardPoints> calculateRewardsForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> allTransactions = transactionRepository.findAllTransactionsByDateRange(startDate, endDate);

        // Group transactions by customer
        Map<String, List<Transaction>> transactionsByCustomer = allTransactions.stream()
                .collect(Collectors.groupingBy(t -> t.getCustomer().getCustomerId()));

        List<RewardPoints> allRewardPoints = new ArrayList<>();

        for (String customerId : transactionsByCustomer.keySet()) {
            List<Transaction> customerTransactions = transactionsByCustomer.get(customerId);
            Optional<Customer> customer = customerRepository.findByCustomerId(customerId);

            if (customer.isPresent()) {
                List<RewardPoints> monthlyRewards = calculateMonthlyRewards(customer.get(), customerTransactions);
                allRewardPoints.addAll(monthlyRewards);
            }
        }

        return allRewardPoints;
    }

    /**
     * Calculate monthly reward points for a specific customer
     */
    public List<RewardPoints> calculateMonthlyRewards(Customer customer, List<Transaction> transactions) {
        Map<YearMonth, Integer> monthlyPointsMap = new HashMap<>();
        int totalPoints = 0;

        for (Transaction transaction : transactions) {
            int points = RewardsCalculator.calculatePoints(transaction.getAmount());
            transaction.setPointsEarned(points);
            totalPoints += points;

            YearMonth yearMonth = YearMonth.from(transaction.getTransactionDate());
            monthlyPointsMap.put(yearMonth, monthlyPointsMap.getOrDefault(yearMonth, 0) + points);
        }

        // Convert to RewardPoints objects
        return monthlyPointsMap.entrySet().stream()
                .map(entry -> new RewardPoints(
                        customer.getCustomerId(),
                        customer.getName(),
                        entry.getKey().getMonthValue(),
                        entry.getKey().getYear(),
                        entry.getValue(),
                        totalPoints
                ))
                .sorted(Comparator.comparing((RewardPoints r) -> r.getYear())
                        .thenComparing(RewardPoints::getMonth))
                .collect(Collectors.toList());
    }

    /**
     * Calculate total reward points for a customer
     */
    public Integer calculateTotalPoints(String customerId) {
        Optional<Customer> customer = customerRepository.findByCustomerId(customerId);
        if (customer.isEmpty()) {
            return 0;
        }

        List<Transaction> transactions = transactionRepository.findByCustomerId(customer.get().getId());
        return transactions.stream()
                .mapToInt(Transaction::getPointsEarned)
                .sum();
    }

    /**
     * Add a transaction for a customer
     */
    public Transaction addTransaction(String customerId, Transaction transaction) {
        Optional<Customer> customer = customerRepository.findByCustomerId(customerId);
        if (customer.isEmpty()) {
            throw new RuntimeException("Customer not found: " + customerId);
        }

        transaction.setCustomer(customer.get());
        int points = RewardsCalculator.calculatePoints(transaction.getAmount());
        transaction.setPointsEarned(points);

        return transactionRepository.save(transaction);
    }
}