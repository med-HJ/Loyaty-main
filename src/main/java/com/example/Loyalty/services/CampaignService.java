package com.example.Loyalty.services;


import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;


import java.util.List;

public interface CampaignService {
    Campaign getCampaignById(Long campaignId);
    Campaign createCampaign(Campaign campaign);
    Campaign updateCampaign(Long campaignId, Campaign campaign);
    void deletecampaign(Long campaignId);
    List<Campaign> getAllCampaigns();
    List<Reward> getAvailableRewardsByCampaignId(Long campaignId) ;
    List<Member> getMembersByCampaignId(Long campaignId);
}

