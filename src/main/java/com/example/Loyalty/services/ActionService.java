package com.example.Loyalty.services;


import com.example.Loyalty.enums.ActionType;
import com.example.Loyalty.models.Action;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ActionService {
    Action createAction(Action action, Optional<Long> targetMember);
    Action updateAction(Long id, Action action, Optional<Long> targetMember);
    boolean deleteAction(Long id);
    Action getActionById(Long id);
    List<Action> getAllActions();
    List<Action> getActionsByType(ActionType actionType);
    List<Action> getActionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    int calculateTotalPointsForMemberByType(Long memberId, ActionType type);
    List<Action> getActionByRewardId(Long rewardId);
    List<Action> getActionByEventId(Long eventId);
    List<Action> getActionByCatalogId(Long catalogId);




    // a verifier List<Action> getActionsByMemberId(Long memberId);
    //List<Action> getActionByBenefitId(Long benefitId);
    // cote admin List<Action> getActionByCampaign(Long campaignId);
}