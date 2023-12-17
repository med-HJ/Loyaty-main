package com.example.Loyalty.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private int totalPoints;

    private int currentPoints;
    private String referralCode;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    @ManyToOne
    private Level level;
    @OneToMany(mappedBy="member",fetch = FetchType.LAZY)
    private List<Action> actions;
    @ManyToMany(mappedBy="members",fetch = FetchType.LAZY)
    private List<Event> events;
    @ManyToMany(mappedBy="members",fetch = FetchType.LAZY)
    private List<Reward> rewards;
    @ManyToMany(mappedBy="members",fetch = FetchType.LAZY)
    private List<Campaign> campaigns;
    @OneToMany(mappedBy = "targetMember", fetch = FetchType.LAZY)
    private List<Movement> movements;
}
