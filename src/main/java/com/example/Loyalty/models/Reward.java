package com.example.Loyalty.models;

import com.example.Loyalty.dtos.LevelDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;

    private String description;

    private int pointsRequired;

    private int stock;
    private LocalDateTime redemptionDate;

    private LocalDate expiryDate;

    @ManyToOne
    private Level level;

    @ManyToMany
    private List<Campaign> campaigns;

    @ManyToMany
    private List<Member> members;

    @ManyToOne
    private Catalog catalog;

    @ManyToMany(mappedBy = "rewards")
    private List<Event> events;

}
