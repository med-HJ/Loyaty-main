package com.example.Loyalty.services;

import com.example.Loyalty.enums.MovementType;
import com.example.Loyalty.models.Movement;
import com.example.Loyalty.repositories.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MovementService {

    @Autowired
    private MovementRepository movementRepository;


    public Movement createMovement(Movement movement) {

        return movementRepository.save(movement);

    }


    public Movement updateMovement(Long id, Movement updatedMovement) {
        Movement existingMovement = movementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Movement not found with id: " + id));

        existingMovement.setAmount(updatedMovement.getAmount());
        existingMovement.setMotif(updatedMovement.getMotif());
        existingMovement.setDirection(updatedMovement.getDirection());

        return movementRepository.save(existingMovement);
    }


    public boolean deleteMovement(Long id) {
        try {
            movementRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }


    public Movement getMovementById(Long id) {
        return movementRepository.findById(id).orElse(null);
    }


    public List<Movement> getMovementsByTargetMember(Long memberId) {
        // Implement this method if needed
        return null;
    }


    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }


    public List<Movement> getMovementsByType(MovementType movementType) {
        return movementRepository.findMovementsByDirectionIgnoreCase(movementType);
    }


    public List<Movement> getMovementsByActionId(Long actionId) {
        return movementRepository.findMovementsByActionId(actionId);
    }
}
