package com.example.Loyalty.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String eventName;

    private LocalDateTime eventDate;

    private String description;
    @ManyToMany
    private List<Member> members;
    @ManyToMany
    private List<Campaign> campaigns;

    @ManyToMany
    private List<Reward> rewards;

}
