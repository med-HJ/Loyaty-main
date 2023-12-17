package com.example.Loyalty.web;

import com.example.Loyalty.dtos.ActionDTO;
import com.example.Loyalty.enums.ActionType;
import com.example.Loyalty.services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/loyalty/actions")

public class ActionController {
    private final ActionService actionService;
    @Autowired
    public ActionController(ActionService actionService){
        this.actionService= actionService;
    }
    @PostMapping
    public ActionDTO createAction(@RequestBody ActionDTO actionDTO,  @RequestParam Optional<Long> targetMember){
        return actionService.createAction(actionDTO, targetMember);
    }
    @PutMapping("/{id}")
    public ActionDTO updateAction(@PathVariable Long id, @RequestBody ActionDTO actionDTO, @RequestParam Optional<Long> targetMember){
        return actionService.updateAction(id, actionDTO, targetMember);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAction(@PathVariable Long id){
        boolean deleted = actionService.deleteAction(id);
        if(deleted){
            return new ResponseEntity<>("Action deleted successfully", HttpStatus.OK);

        }else {
            return new ResponseEntity<>("Failed to delete action", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getActionById(@PathVariable Long id){
        if(id== null){
            return  new ResponseEntity<>("Id is required", HttpStatus.BAD_REQUEST);
        }
        ActionDTO actionDTO=actionService.getActionById(id);

        if(actionDTO == null){
            return new ResponseEntity<>("Action not found", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(actionDTO, HttpStatus.OK);
        }

    }
    @GetMapping
    public ResponseEntity<List<ActionDTO>> getAllActions(){
        List<ActionDTO> actions= actionService.getAllActions();
        if(actions.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(actions,HttpStatus.OK);
        }
    }
    @GetMapping("/type/{actionType}")
    public ResponseEntity<List<ActionDTO>> getActionsByType(@PathVariable String actionType){
        ActionType enumActionType;
        try{
            enumActionType= ActionType.valueOf(actionType.toUpperCase());
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<ActionDTO> actionDTOS= actionService.getActionsByType(enumActionType);
        if(actionDTOS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(actionDTOS, HttpStatus.OK);
        }
    }
    @GetMapping("/by-date-range")
    public ResponseEntity<List<ActionDTO>>getActionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ){
        List<ActionDTO> actionDTOS= actionService.getActionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(actionDTOS);

    }

    @GetMapping("/calculate-points")
    public ResponseEntity<Integer> calculateTotalPointsForMemberByType(@RequestParam Long id, @RequestParam String actionType){
        ActionType enumActionType;
        try{
            enumActionType= ActionType.valueOf(actionType.toUpperCase());
        }catch (IllegalArgumentException ex){
            throw ex;
        }
        int totalPointsByType= actionService.calculateTotalPointsForMemberByType(id, enumActionType);
        return ResponseEntity.ok(totalPointsByType);
    }
}