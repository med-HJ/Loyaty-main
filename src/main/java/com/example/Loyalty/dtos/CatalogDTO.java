package com.example.Loyalty.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;
@Data
public class CatalogDTO {
    private Long id;
    private String description;
    @JsonIgnoreProperties("catalog")
    private List<RewardDTO> rewards;

}
