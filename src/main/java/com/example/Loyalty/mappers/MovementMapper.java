package com.example.Loyalty.mappers;

import com.example.Loyalty.dtos.MovementDTO;
import com.example.Loyalty.models.Movement;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MovementMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    public static Movement convertToMovement(MovementDTO movementDTO){
        return modelMapper.map(movementDTO, Movement.class);
    }
    public static MovementDTO convertToMovementDTO(Movement movement){
        return modelMapper.map(movement, MovementDTO.class);
    }

}
