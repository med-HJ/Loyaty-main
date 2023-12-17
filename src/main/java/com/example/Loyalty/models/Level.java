package com.example.Loyalty.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;

    private int minPoints;

    private LocalDateTime validityPeriod;

    private int customerCount;



    @OneToMany(mappedBy="level",fetch = FetchType.LAZY)
    private List<Member> members;

    @OneToMany(mappedBy="level",fetch = FetchType.LAZY)
    private List<Reward> rewards;
    @OneToMany(mappedBy="level",fetch = FetchType.LAZY)
    private List<Benefit> benefits;


}
