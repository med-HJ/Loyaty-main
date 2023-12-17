package com.example.Loyalty.services;

import com.example.Loyalty.dtos.CampaignDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Campaign;

import java.util.List;
public interface RewardService {
    RewardDTO getRewardById (Long rewardId);
    RewardDTO createReward(RewardDTO rewardDTO, Long levelId);

    RewardDTO updateReward(Long rewardId, RewardDTO rewardDTO, Long levelId);
    void  deleteReward(Long RewardId);
    List<RewardDTO> getAllRewards();
    List<CampaignDTO> getCampaignsByRewardId(Long rewardId);
    List<MemberDTO> getMembersByRewardId(Long RewardId);
}
