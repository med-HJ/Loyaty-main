package com.example.Loyalty.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class EventDTO {
    private Long id;
    private String eventName;
    private LocalDateTime eventDate;
    private String description;
    private List<Long> membersId;
    private List<Long> campaignsId;
    private List<Long> rewardsId;

}
