package com.retailer.rewards.controller;

import com.retailer.rewards.model.RewardPoints;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.service.RewardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardsController {

    private final RewardsService rewardsService;

    /**
     * Calculate rewards for a date range
     * Example: /api/rewards/calculate?startDate=2024-01-01T00:00:00&endDate=2024-03-31T23:59:59
     */
    @GetMapping("/calculate")
    public ResponseEntity<List<RewardPoints>> calculateRewards(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<RewardPoints> rewards = rewardsService.calculateRewardsForPeriod(startDate, endDate);
        return ResponseEntity.ok(rewards);
    }

    /**
     * Get total points for a customer
     */
    @GetMapping("/customer/{customerId}/total")
    public ResponseEntity<Integer> getTotalPoints(@PathVariable String customerId) {
        Integer totalPoints = rewardsService.calculateTotalPoints(customerId);
        return ResponseEntity.ok(totalPoints);
    }

    /**
     * Add a transaction for a customer
     */
    @PostMapping("/customer/{customerId}/transaction")
    public ResponseEntity<Transaction> addTransaction(
            @PathVariable String customerId,
            @RequestBody Transaction transaction) {
        Transaction savedTransaction = rewardsService.addTransaction(customerId, transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }
}