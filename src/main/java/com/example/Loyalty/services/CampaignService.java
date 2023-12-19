package com.example.Loyalty.services;

import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.repositories.CampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }


    public Campaign getCampaignById(Long campaignId) {
        return campaignRepository.findById(campaignId).orElse(null);
    }


    public Campaign createCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }


    public Campaign updateCampaign(Long campaignId, Campaign updatedCampaign) {
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElse(null);
        if (existingCampaign != null) {
            existingCampaign.setName(updatedCampaign.getName());
            existingCampaign.setStartDate(updatedCampaign.getStartDate());
            existingCampaign.setEndDate(updatedCampaign.getEndDate());
            existingCampaign.setDescription(updatedCampaign.getDescription());

            return campaignRepository.save(existingCampaign);
        }
        return null;
    }


    public void deletecampaign(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
        if (campaign != null) {
            campaignRepository.delete(campaign);
        }
    }


    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }


    public List<Reward> getAvailableRewardsByCampaignId(Long campaignId) {
        try {
            Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
            return new ArrayList<>(campaign.getRewards());
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }


    public List<Member> getMembersByCampaignId(Long campaignId) {
        try {
            Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
            return new ArrayList<>(campaign.getMembers());
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }
}
