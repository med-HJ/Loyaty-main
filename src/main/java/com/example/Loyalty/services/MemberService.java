package com.example.Loyalty.services;


import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;

import java.util.List;

public interface MemberService {
    Member getById(Long id);
    List<Member> getAllMembers();
    Member saveMember(Member member);
    Boolean deleteMember(Long id);
    Member updateMember(Member member);

    void earnPoints(Long memberId, String activityName, int pointsEarned);
    void redeemPointsByReward(Long memberId, Long rewardId);

    int getPointsBalanceByMemberId(Long memberId);
    List<Reward> getRedemptionHistoryByMemberId(Long memberId);
    int countMembersByLevel(Long levelId);
    void transferPoints(Long sourceMemberId, Long destinationMemberId, int pointsToTransfer) throws Exception;


}
