package com.example.Loyalty.services;


import com.example.Loyalty.dtos.MovementDTO;
import com.example.Loyalty.enums.MovementType;


import java.util.List;

public interface MovementService {
    MovementDTO createMovement(MovementDTO movementDTO);
    MovementDTO updateMovement(Long id, MovementDTO movementDTO);
    boolean deleteMovement(Long id);
    MovementDTO getMovementById(Long id);
    List<MovementDTO> getMovementsByTargetMember(Long memberId);
    List<MovementDTO> getAllMovements();
    List<MovementDTO> getMovementsByType(MovementType movementType);
    List<MovementDTO> getMovementsByActionId(Long actionId);


}
