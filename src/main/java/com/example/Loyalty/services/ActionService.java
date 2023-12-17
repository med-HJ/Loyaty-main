package com.example.Loyalty.services;

import com.example.Loyalty.dtos.ActionDTO;
import com.example.Loyalty.enums.ActionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ActionService {
    ActionDTO createAction(ActionDTO actionDTO, Optional<Long> targetMember);
    ActionDTO updateAction(Long id, ActionDTO actionDTO, Optional<Long> targetMember);
    boolean deleteAction(Long id);
    ActionDTO getActionById(Long id);
    List<ActionDTO> getAllActions();
    List<ActionDTO> getActionsByType(ActionType actionType);
    List<ActionDTO> getActionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    int calculateTotalPointsForMemberByType(Long memberId, ActionType type);
    List<ActionDTO> getActionByRewardId(Long rewardId);
    List<ActionDTO> getActionByEventId(Long eventId);
    List<ActionDTO> getActionByCatalogId(Long catalogId);




    // a verifier List<Action> getActionsByMemberId(Long memberId);
    //List<Action> getActionByBenefitId(Long benefitId);
    // cote admin List<Action> getActionByCampaign(Long campaignId);
}