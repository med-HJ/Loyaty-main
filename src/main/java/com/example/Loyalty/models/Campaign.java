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
@Table(name = "campaigns")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    @ManyToMany(mappedBy="campaigns",fetch = FetchType.LAZY)
    private List<Event> events;
    @ManyToMany(mappedBy="campaigns",fetch = FetchType.LAZY)
    private List<Reward> rewards;
    @ManyToMany
    private List<Member> members;
}
