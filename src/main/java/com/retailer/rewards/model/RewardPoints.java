package com.retailer.rewards.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardPoints {

    private String customerId;
    private String customerName;
    private Integer month;
    private Integer year;
    private Integer monthlyPoints;
    private Integer totalPoints;
}