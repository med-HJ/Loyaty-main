package com.example.Loyalty.models;

import com.example.Loyalty.enums.ActionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private LocalDateTime date;
    private String description;
    private int points;
    @Enumerated(EnumType.STRING)
    private ActionType type;
    @JsonIgnore
    @ManyToOne
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy="action",fetch = FetchType.LAZY)
    private List<Movement> movements;

}
