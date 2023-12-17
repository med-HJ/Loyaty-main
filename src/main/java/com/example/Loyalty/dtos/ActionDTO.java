package com.example.Loyalty.dtos;
import com.example.Loyalty.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionDTO {
        private Long id;
        private String name;
        private LocalDateTime date;
        private String description;
        private int points;
        private ActionType type;
        private Long memberId;

}
