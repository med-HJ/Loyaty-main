package com.example.Loyalty.dtos;

import com.example.Loyalty.models.Level;
import lombok.Data;

import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MemberDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private int totalPoints;
    private int currentPoints;
    private String referralCode;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    private Long levelId;

}
