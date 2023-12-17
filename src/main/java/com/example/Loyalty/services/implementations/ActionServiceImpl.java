package com.example.Loyalty.services.implementations;
import com.example.Loyalty.enums.ActionType;
import com.example.Loyalty.enums.MovementType;

import com.example.Loyalty.models.Action;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Movement;
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
    private final MovementService movementService;
    @Autowired
    public ActionServiceImpl(
            ActionRepository actionRepository,
            MovementService movementService,
            MemberRepository memberRepository
    ) {
        this.actionRepository = actionRepository;
        this.movementService = movementService;
        this.memberRepository = memberRepository;
    }




    @Override
    public Action createAction(Action action, Optional<Long> targetMember) {
        action.setDate(LocalDateTime.now());
        Action savedAction = actionRepository.save(action);

        ActionType actionType = action.getType();
        if (actionType == ActionType.EARN) {
            Movement movement = new Movement();
            movement.setMotif(action.getName());
            movement.setAmount(action.getPoints());
            movement.setDirection(MovementType.CREDIT);
            movement.setAction(savedAction);
            movement.setTargetMember(targetMember.isPresent() ? memberRepository.findById(targetMember.get()).orElse(null) : null);

            movementService.createMovement(movement);

        } else if (actionType == ActionType.BURN) {
            Movement movement = new Movement();
            movement.setMotif(action.getName());
            movement.setAmount(action.getPoints());
            movement.setDirection(MovementType.DEBIT);
            movement.setAction(savedAction);

            movementService.createMovement(movement);

        } else if (actionType == ActionType.TRANSFER && targetMember.isPresent()) {
            String transferMotif = String.format("Transfer point to %s", targetMember.get());
            String creditMotif = String.format("Transfer from %s", action.getMember().getId());
            Movement movement = new Movement();
            movement.setMotif(transferMotif);
            movement.setAmount(action.getPoints());
            movement.setDirection(MovementType.DEBIT);


            movement.setDirection(MovementType.CREDIT);
            movement.setAction(savedAction);
            movement.setTargetMember(targetMember.isPresent() ? memberRepository.findById(targetMember.get()).orElse(null) : null);

            movementService.createMovement(movement);

        }

        return savedAction;
    }

    @Override
    public Action updateAction(Long id, Action updatedAction, Optional<Long> targetMember) {
        Optional<Action> isActionExist = actionRepository.findActionById(id);
        if (isActionExist.isPresent()) {
            Action existingAction = isActionExist.get();
            existingAction.setName(updatedAction.getName());
            existingAction.setDate(updatedAction.getDate());
            existingAction.setDescription(updatedAction.getDescription());
            existingAction.setType(updatedAction.getType());
            existingAction.setPoints(updatedAction.getPoints());

            List<Movement> movements = movementService.getMovementsByActionId(id);

            // Updating the associated movement based on the type of action.
            if (existingAction.getType() == ActionType.EARN || existingAction.getType() == ActionType.BURN) {
                if (movements.size() == 1) {
                    Movement movement = movements.get(0);
                    movement.setMotif(existingAction.getName());
                    movement.setAmount(existingAction.getPoints());
                    MovementType movementType = (existingAction.getType() == ActionType.EARN) ? MovementType.CREDIT : MovementType.DEBIT;
                    movement.setDirection(movementType);
                    movementService.updateMovement(movement.getId(), movement);
                }
            } else if (existingAction.getType() == ActionType.TRANSFER) {
                Movement debitMovement = movements.get(0);
                debitMovement.setMotif("Transfer point to " + targetMember.orElseThrow());
                debitMovement.setAmount(existingAction.getPoints());
                debitMovement.setDirection(MovementType.DEBIT);
                movementService.updateMovement(debitMovement.getId(), debitMovement);

                Movement creditMovement = movements.get(1);
                creditMovement.setMotif("Transfer from " + existingAction.getMember().getId());
                creditMovement.setAmount(existingAction.getPoints());
                creditMovement.setDirection(MovementType.CREDIT);
                creditMovement.setTargetMember(targetMember.map(Member::new).orElseThrow());
                movementService.updateMovement(creditMovement.getId(), creditMovement);
            }

            return actionRepository.save(existingAction);
        } else {
            throw new NoSuchElementException("Action not found with ID: " + id);
        }
    }

    @Override
    public boolean deleteAction(Long id) {
        try {
            List<Movement> movements = movementService.getMovementsByActionId(id);
            for (Movement movement : movements) {
                movementService.deleteMovement(movement.getId());
            }
            actionRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Action getActionById(Long id) {
        return actionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Action> getAllActions() {
        try {
            return actionRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Action> getActionsByType(ActionType actionType) {
        try {
            return actionRepository.findActionsByTypeIgnoreCase(actionType);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Action> getActionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return actionRepository.getActionByDateRange(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public int calculateTotalPointsForMemberByType(Long memberId, ActionType type) {
        try {
            List<Action> actionMemberType = actionRepository.findActionsByTypeIgnoreCase(type);
            int count = 0;
            for (Action action : actionMemberType) {
                if (action.getMember().getId() == memberId) {
                    count += action.getPoints();
                }
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Action> getActionByRewardId(Long rewardId) {
        return null;
    }
    @Override
    public List<Action> getActionByEventId(Long eventId) {
        return null;
    }
    @Override
    public List<Action> getActionByCatalogId(Long catalogId) {
        return null;
    }
}