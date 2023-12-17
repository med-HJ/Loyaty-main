package com.example.Loyalty.dtos;


import com.example.Loyalty.models.Catalog;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RewardDTO {
    private Long id;

    private String name;

    private String description;

    private int pointsRequired;

    private int stock;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private Long levelId;

    @JsonIgnoreProperties("rewards")
    private List<CampaignDTO> campaigns;

    @JsonIgnoreProperties("rewards")
    private List<MemberDTO> members;

    private CatalogDTO catalogDTO;

    @JsonIgnoreProperties("rewards")
    private List<EventDTO> events;

}
