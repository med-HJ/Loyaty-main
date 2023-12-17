package com.example.Loyalty.services;



import com.example.Loyalty.models.Reward;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Campaign;

import java.util.List;
public interface RewardService {
    Reward getRewardById (Long rewardId);
    Reward createReward(Reward reward, Long levelId);

    Reward updateReward(Long rewardId, Reward reward, Long levelId);
    void  deleteReward(Long RewardId);
    List<Reward> getAllRewards();
    List<Campaign> getCampaignsByRewardId(Long rewardId);
    List<Member> getMembersByRewardId(Long RewardId);
}
