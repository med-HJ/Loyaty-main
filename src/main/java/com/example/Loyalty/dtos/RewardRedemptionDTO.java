package com.example.Loyalty.dtos;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RewardRedemptionDTO {
    private Long rewardId;
    private String rewardName;
    private LocalDateTime redemptionDate;
}
