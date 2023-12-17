package com.example.Loyalty.repositories;

import com.example.Loyalty.enums.MovementType;
import com.example.Loyalty.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findAll() ;
    Optional<Movement> findById(Long id) ;
    Movement save(Movement movement);
    void delete(Movement movement);
    @Query("SELECT m FROM Movement m WHERE m.direction = :movementType")
    List<Movement> findMovementsByDirectionIgnoreCase(MovementType movementType);
    @Query("SELECT m from Movement m WHERE m.action.id = :actionId")
    List<Movement> findMovementsByActionId(@Param("actionId") Long actionId);

}
