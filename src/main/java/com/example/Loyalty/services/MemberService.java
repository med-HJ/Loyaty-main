package com.example.Loyalty.services;

import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardRedemptionDTO;

import java.util.List;

public interface MemberService {
    MemberDTO getById(Long id);
    List<MemberDTO> getAllMembers();
    MemberDTO saveMember(MemberDTO memberDTO);
    Boolean deleteMember(Long id);
    MemberDTO updateMember(MemberDTO memberDTO);

    void earnPoints(Long memberId, String activityName, int pointsEarned);
    void redeemPointsByReward(Long memberId, Long rewardId);

    int getPointsBalanceByMemberId(Long memberId);
    List<RewardRedemptionDTO> getRedemptionHistoryByMemberId(Long memberId);
    int countMembersByLevel(Long levelId);
    void transferPoints(Long sourceMemberId, Long destinationMemberId, int pointsToTransfer) throws Exception;


}
