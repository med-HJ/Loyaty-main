package com.example.Loyalty.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LevelDTO {
    private Long id;

    private String name;

    private int minPoints;

    private LocalDateTime validityPeriod;

    private int customerCount;
    @JsonIgnoreProperties("level")
    private List<RewardDTO> rewards;

}
