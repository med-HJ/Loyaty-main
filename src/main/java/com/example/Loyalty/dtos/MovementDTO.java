package com.example.Loyalty.dtos;

import com.example.Loyalty.enums.MovementType;
import com.example.Loyalty.models.Action;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementDTO {
    private Long id;
    private String motif;
    private int amount;
    private MovementType direction;
    private Long actionId;
    private Long targetMemberId;


}
