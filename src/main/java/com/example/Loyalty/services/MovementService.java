package com.example.Loyalty.services;



import com.example.Loyalty.enums.MovementType;
import com.example.Loyalty.models.Movement;


import java.util.List;

public interface MovementService {
    Movement createMovement(Movement movement);
    Movement updateMovement(Long id, Movement movement);
    boolean deleteMovement(Long id);
    Movement getMovementById(Long id);
    List<Movement> getMovementsByTargetMember(Long memberId);
    List<Movement> getAllMovements();
    List<Movement> getMovementsByType(MovementType movementType);
    List<Movement> getMovementsByActionId(Long actionId);


}
