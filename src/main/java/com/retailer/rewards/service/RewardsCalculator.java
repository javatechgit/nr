package com.retailer.rewards.service;

import java.math.BigDecimal;

public class RewardsCalculator {

    /**
     * Calculate reward points based on transaction amount.
     *
     * Rules:
     * - 2 points for every dollar spent over $100
     * - 1 point for every dollar spent between $50 and $100
     * - 0 points for purchases $50 or less
     *
     * Example: $120 purchase = 2x$20 (over $100) + 1x$50 (between $50-$100) = 90 points
     *
     * @param amount Transaction amount
     * @return Points earned for this transaction
     */
    public static Integer calculatePoints(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        int points = 0;
        BigDecimal threshold100 = new BigDecimal("100");
        BigDecimal threshold50 = new BigDecimal("50");

        // 2 points for every dollar over $100
        if (amount.compareTo(threshold100) > 0) {
            BigDecimal amountOver100 = amount.subtract(threshold100);
            points += amountOver100.intValue() * 2;
        }

        // 1 point for every dollar between $50 and $100
        if (amount.compareTo(threshold50) > 0) {
            BigDecimal maxThreshold = amount.compareTo(threshold100) > 0 ? threshold100 : amount;
            BigDecimal amountBetween50And100 = maxThreshold.subtract(threshold50);
            points += amountBetween50And100.intValue();
        }

        return points;
    }
}