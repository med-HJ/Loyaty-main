package com.example.Loyalty.services;

import com.example.Loyalty.dtos.CampaignDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;


import java.util.List;

public interface CampaignService {
    CampaignDTO getCampaignById(Long campaignId);
    CampaignDTO createCampaign(CampaignDTO campaign);
    CampaignDTO updateCampaign(Long campaignId, CampaignDTO campaignDTO);
    void deletecampaign(Long campaignId);
    List<CampaignDTO> getAllCampaigns();
    List<RewardDTO> getAvailableRewardsByCampaignId(Long campaignId) ;
    List<MemberDTO> getMembersByCampaignId(Long campaignId);
}

