package com.example.Loyalty.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class CampaignDTO {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    private String description;
    @JsonIgnoreProperties("campaigns")
    private List<MemberDTO> members;
    @JsonIgnoreProperties("campaigns")
    private List<RewardDTO> rewards;
    @JsonIgnoreProperties("campaigns")
    private List<EventDTO> events;
}
