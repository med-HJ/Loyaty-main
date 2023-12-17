package com.example.Loyalty.services.implementations;
import com.example.Loyalty.dtos.ActionDTO;
import com.example.Loyalty.dtos.MovementDTO;
import com.example.Loyalty.enums.ActionType;
import com.example.Loyalty.enums.MovementType;
import com.example.Loyalty.mappers.ActionMapper;
import com.example.Loyalty.models.Action;
import com.example.Loyalty.repositories.ActionRepository;
import com.example.Loyalty.repositories.MemberRepository;
import com.example.Loyalty.services.ActionService;
import com.example.Loyalty.services.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
@Transactional
@Service
public class ActionServiceImpl implements ActionService {
    private final ActionRepository actionRepository;
    private final MemberRepository memberRepository;
    private final ActionMapper actionMapper;
    private final MovementService movementService;
    @Autowired
    public ActionServiceImpl(ActionRepository actionRepository, MovementService movementService, ActionMapper actionMapper, MemberRepository memberRepository){
        this.actionRepository= actionRepository;
        this.movementService= movementService;
        this.actionMapper= actionMapper;
        this.memberRepository= memberRepository;
    }

    private void createDebitMovement(Action savedAction, String motif,int amount ){
        MovementDTO movementDTO= new MovementDTO();
        movementDTO.setMotif(motif);
        movementDTO.setAmount(amount);
        movementDTO.setDirection(MovementType.DEBIT);
        movementDTO.setActionId(savedAction.getId());
        movementService.createMovement(movementDTO);
    }
    private void createCreditMovement(Action savedAction, String motif,int amount, Optional<Long> targetMember){
        MovementDTO movementDTO= new MovementDTO();
        movementDTO.setMotif(motif);
        movementDTO.setAmount(amount);
        movementDTO.setDirection(MovementType.CREDIT);
        movementDTO.setActionId(savedAction.getId());
        movementDTO.setTargetMemberId(targetMember.orElse(null));
        movementService.createMovement(movementDTO);
    }
    @Override
    public ActionDTO createAction(ActionDTO actionDTO, Optional<Long> targetMember) {
        Action action = actionMapper.convertToAction(actionDTO);
        action.setDate(LocalDateTime.now());
        Action savedAction=  actionRepository.save(action);

        ActionType actionType= action.getType();
        if(actionType == ActionType.EARN ){
            createCreditMovement(savedAction, action.getName(), action.getPoints(), targetMember);
        }
        else if(actionType== ActionType.BURN){
            createDebitMovement(savedAction, action.getName(), action.getPoints());
        }
        else if (actionType == ActionType.TRANSFER && targetMember.isPresent()) {
            String transferMotif= String.format("Transfer point to %s", targetMember.get());
            String creditMotif= String.format("Transfer from %s", action.getMember().getId());
            createDebitMovement(savedAction, transferMotif, action.getPoints());
            createCreditMovement(savedAction, creditMotif, action.getPoints(), targetMember);

        }

        return actionMapper.convertToActionDTO(savedAction);
    }
    @Override
    public ActionDTO updateAction(Long id, ActionDTO actionDTO, Optional<Long> targetMember) {
        Optional<Action> isActionExist = actionRepository.findActionById(id);
        if (isActionExist.isPresent()){
            Action updateAction= isActionExist.get();
            updateAction.setName(actionDTO.getName());
            updateAction.setDate(actionDTO.getDate());
            updateAction.setDescription(actionDTO.getDescription());
            updateAction.setType(actionDTO.getType());
            updateAction.setPoints(actionDTO.getPoints());
            List<MovementDTO> movementDTOS = movementService.getMovementsByActionId(id);
            //Updating the associated movement based on the type of action.
            if(updateAction.getType()== ActionType.EARN || updateAction.getType()== ActionType.BURN){
                if(movementDTOS.size()==1){
                    MovementDTO movementDTO= movementDTOS.get(0);
                    movementDTO.setMotif(updateAction.getName());
                    movementDTO.setAmount(updateAction.getPoints());
                    MovementType movementType=(updateAction.getType()== ActionType.EARN) ? MovementType.CREDIT: MovementType.DEBIT;
                    movementDTO.setDirection(movementType);
                    movementService.updateMovement(movementDTO.getId(), movementDTO);
                }
            } else if (updateAction.getType()== ActionType.TRANSFER) {
                MovementDTO debitMovement = movementDTOS.get(0);
                debitMovement.setMotif("Transfer point to " + targetMember.get() );
                debitMovement.setAmount(updateAction.getPoints());
                debitMovement.setDirection(MovementType.DEBIT);
                movementService.updateMovement(debitMovement.getId(), debitMovement);
                MovementDTO creditMovement = movementDTOS.get(1);
                creditMovement.setMotif("Transfer from " + updateAction.getMember().getId());
                creditMovement.setAmount(updateAction.getPoints());
                creditMovement.setDirection(MovementType.CREDIT);
                creditMovement.setTargetMemberId(targetMember.get());
                movementService.updateMovement(creditMovement.getId(), creditMovement);
            }
            return actionMapper.convertToActionDTO(actionRepository.save(updateAction));
        }else {
            throw new NoSuchElementException("Action not found with ID: "+ id);
        }
    }
    @Override
    public boolean deleteAction(Long id) {
        try{
            List<MovementDTO> movements= movementService.getMovementsByActionId(id);
            for(MovementDTO movement: movements){
                movementService.deleteMovement(movement.getId());
            }
            actionRepository.deleteById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public ActionDTO getActionById(Long id) {
        Optional<Action> action= actionRepository.findById(id);
        if(action.isPresent()){
            ActionDTO actionDTO= actionMapper.convertToActionDTO(action.get());
            return actionDTO ;
        }
        else {
            return null;
        }
    }
    @Override
    public List<ActionDTO> getAllActions() {
        try {
            List<Action> actions = actionRepository.findAll();
            List<ActionDTO> actionDTOs = new ArrayList<>();
            for (Action action : actions) {
                ActionDTO actionDTO = actionMapper.convertToActionDTO(action);
                actionDTOs.add(actionDTO);
            }
            return actionDTOs;
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public List<ActionDTO> getActionsByType(ActionType actionType) {
        try{
            List<Action> actions= actionRepository.findActionsByTypeIgnoreCase(actionType);
            List<ActionDTO> actionDTOS= new ArrayList<>();
            for(Action action: actions){
                actionDTOS.add(actionMapper.convertToActionDTO(action));
            }
            return actionDTOS;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public List<ActionDTO> getActionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try{
            List<Action> actions= actionRepository.getActionByDateRange(startDate,endDate);
            List<ActionDTO> actionDTOS= new ArrayList<>();
            for(Action action: actions){
                actionDTOS.add(actionMapper.convertToActionDTO(action));
            }
            return  actionDTOS;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public int calculateTotalPointsForMemberByType(Long memberId, ActionType type) {
        try{
            List<Action> actionMemberType= actionRepository.findActionsByTypeIgnoreCase(type);
            List<ActionDTO> actionDTOS= new ArrayList<>();
            int count =0;
            for(Action action: actionMemberType){
                actionDTOS.add(actionMapper.convertToActionDTO(action));
                if(action.getMember().getId()== memberId){
                    count+=action.getPoints();
                }
            }
            return count;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @Override
    public List<ActionDTO> getActionByRewardId(Long rewardId) {
        return null;
    }
    @Override
    public List<ActionDTO> getActionByEventId(Long eventId) {
        return null;
    }
    @Override
    public List<ActionDTO> getActionByCatalogId(Long catalogId) {
        return null;
    }
}