package com.example.Loyalty.web;

import com.example.Loyalty.dtos.ActionDTO;
import com.example.Loyalty.dtos.MovementDTO;
import com.example.Loyalty.enums.MovementType;
import com.example.Loyalty.models.Movement;
import com.example.Loyalty.services.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/loyalty/movements")
public class MovementController {
    private final MovementService movementService;
    @Autowired
    public MovementController(MovementService movementService){
        this.movementService= movementService;
    }
    @PostMapping
    public MovementDTO createMovement(@RequestBody MovementDTO movementDTO){
        return movementService.createMovement(movementDTO);
    }
    @GetMapping
    public List<MovementDTO> getAllMovement(){
        return  movementService.getAllMovements();
    }

    @DeleteMapping("/{id}")
    public boolean deleteMovement(@PathVariable Long id){
        return movementService.deleteMovement(id);
    }
    @PutMapping("/{id}")
    public MovementDTO updateMovement(@PathVariable Long id,@RequestBody MovementDTO movementDTO){
        MovementDTO updateMovement= movementService.updateMovement(id, movementDTO);
        return updateMovement;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovementById(@PathVariable Long id){
        if(id == null){
            return new ResponseEntity<>("Id is required", HttpStatus.BAD_REQUEST);
        }
        MovementDTO movementDTO= movementService.getMovementById(id);
        if(movementDTO == null){
            return new ResponseEntity<>("Movement not found", HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(movementDTO, HttpStatus.OK);
        }

    }

    @GetMapping("/direction/{type}")
    public ResponseEntity<List<MovementDTO>> getMovementByDirection(@PathVariable String type){
        MovementType enumMovement;
        try{
            enumMovement= MovementType.valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<MovementDTO> movementDTOS= movementService.getMovementsByType(enumMovement);
        if(movementDTOS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(movementDTOS, HttpStatus.OK);
        }
    }

    @GetMapping("/action/{actionId}")
    public ResponseEntity<?> getMovementByActionId(@PathVariable Long actionId){
        if(actionId== null){
            return new ResponseEntity<>("Id is required", HttpStatus.BAD_REQUEST);
        }
        List<MovementDTO> movementDTOs= movementService.getMovementsByActionId(actionId);
        if(movementDTOs.isEmpty()){
            return new ResponseEntity<>("No movement associated with the action was found", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(movementDTOs, HttpStatus.OK);
        }

    }

}