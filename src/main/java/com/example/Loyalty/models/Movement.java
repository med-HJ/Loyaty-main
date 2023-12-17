package com.example.Loyalty.models;

import com.example.Loyalty.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String motif;
    private double amount;

    @Enumerated(EnumType.STRING)
    private MovementType direction;
    @ManyToOne
    private Action action;

    @ManyToOne
    @JoinColumn(name = "targetMember")
    private Member targetMember;
}
