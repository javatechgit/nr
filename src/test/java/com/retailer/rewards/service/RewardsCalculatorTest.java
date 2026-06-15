package com.retailer.rewards.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RewardsCalculatorTest {

    @Test
    public void testCalculatePoints_NoPoints_Under50() {
        // Purchases under $50 should earn 0 points
        assertEquals(0, RewardsCalculator.calculatePoints(new BigDecimal("49.99")));
        assertEquals(0, RewardsCalculator.calculatePoints(new BigDecimal("0")));
    }

    @Test
    public void testCalculatePoints_OnePointPerDollar_50To100() {
        // $50 should give 0 points (at threshold)
        assertEquals(0, RewardsCalculator.calculatePoints(new BigDecimal("50")));

        // $51 should give 1 point
        assertEquals(1, RewardsCalculator.calculatePoints(new BigDecimal("51")));

        // $75 should give 25 points (1 point per dollar from $50-$75)
        assertEquals(25, RewardsCalculator.calculatePoints(new BigDecimal("75")));

        // $100 should give 50 points
        assertEquals(50, RewardsCalculator.calculatePoints(new BigDecimal("100")));
    }

    @Test
    public void testCalculatePoints_TwoPointsPerDollar_Over100() {
        // $101 should give 50 + 2 = 52 points
        // (1 point for each dollar from $50-$100 = 50 points, 2 points for $1 over = 2 points)
        assertEquals(52, RewardsCalculator.calculatePoints(new BigDecimal("101")));

        // $120 should give 50 + 40 = 90 points
        // (1 point for each dollar from $50-$100 = 50 points, 2 points for each dollar from $100-$120 = 40 points)
        assertEquals(90, RewardsCalculator.calculatePoints(new BigDecimal("120")));

        // $150 should give 50 + 100 = 150 points
        assertEquals(150, RewardsCalculator.calculatePoints(new BigDecimal("150")));
    }

    @Test
    public void testCalculatePoints_NullAmount() {
        assertEquals(0, RewardsCalculator.calculatePoints(null));
    }

    @Test
    public void testCalculatePoints_NegativeAmount() {
        assertEquals(0, RewardsCalculator.calculatePoints(new BigDecimal("-10")));
    }
}