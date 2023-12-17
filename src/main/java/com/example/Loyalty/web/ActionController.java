package com.example.Loyalty.web;


import com.example.Loyalty.enums.ActionType;
import com.example.Loyalty.models.Action;
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
    public Action createAction(@RequestBody Action action, @RequestParam Optional<Long> targetMember){
        return actionService.createAction(action, targetMember);
    }
    @PutMapping("/{id}")
    public Action updateAction(@PathVariable Long id, @RequestBody Action action, @RequestParam Optional<Long> targetMember){
        return actionService.updateAction(id, action, targetMember);
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
        Action action=actionService.getActionById(id);

        if(action == null){
            return new ResponseEntity<>("Action not found", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(action, HttpStatus.OK);
        }

    }
    @GetMapping
    public ResponseEntity<List<Action>> getAllActions(){
        List<Action> actions= actionService.getAllActions();
        if(actions.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(actions,HttpStatus.OK);
        }
    }
    @GetMapping("/type/{actionType}")
    public ResponseEntity<List<Action>> getActionsByType(@PathVariable String actionType){
        ActionType enumActionType;
        try{
            enumActionType= ActionType.valueOf(actionType.toUpperCase());
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Action> actionS= actionService.getActionsByType(enumActionType);
        if(actionS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(actionS, HttpStatus.OK);
        }
    }
    @GetMapping("/by-date-range")
    public ResponseEntity<List<Action>>getActionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ){
        List<Action> actionS= actionService.getActionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(actionS);

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