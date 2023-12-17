package com.example.Loyalty.web;


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
    public Movement createMovement(@RequestBody Movement movement){
        return movementService.createMovement(movement);
    }
    @GetMapping
    public List<Movement> getAllMovement(){
        return  movementService.getAllMovements();
    }

    @DeleteMapping("/{id}")
    public boolean deleteMovement(@PathVariable Long id){
        return movementService.deleteMovement(id);
    }
    @PutMapping("/{id}")
    public Movement updateMovement(@PathVariable Long id,@RequestBody Movement movement){
        Movement updateMovement= movementService.updateMovement(id, movement);
        return updateMovement;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovementById(@PathVariable Long id){
        if(id == null){
            return new ResponseEntity<>("Id is required", HttpStatus.BAD_REQUEST);
        }
        Movement movement= movementService.getMovementById(id);
        if(movement == null){
            return new ResponseEntity<>("Movement not found", HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(movement, HttpStatus.OK);
        }

    }

    @GetMapping("/direction/{type}")
    public ResponseEntity<List<Movement>> getMovementByDirection(@PathVariable String type){
        MovementType enumMovement;
        try{
            enumMovement= MovementType.valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Movement> movementS= movementService.getMovementsByType(enumMovement);
        if(movementS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(movementS, HttpStatus.OK);
        }
    }

    @GetMapping("/action/{actionId}")
    public ResponseEntity<?> getMovementByActionId(@PathVariable Long actionId){
        if(actionId== null){
            return new ResponseEntity<>("Id is required", HttpStatus.BAD_REQUEST);
        }
        List<Movement> movements= movementService.getMovementsByActionId(actionId);
        if(movements.isEmpty()){
            return new ResponseEntity<>("No movement associated with the action was found", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(movements, HttpStatus.OK);
        }

    }

}