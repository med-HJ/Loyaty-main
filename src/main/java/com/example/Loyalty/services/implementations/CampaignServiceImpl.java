package com.example.Loyalty.services.implementations;

import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.repositories.CampaignRepository;
import com.example.Loyalty.services.CampaignService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public Campaign getCampaignById(Long campaignId) {
        return campaignRepository.findById(campaignId).orElse(null);
    }

    @Override
    public Campaign createCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
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

    @Override
    public void deletecampaign(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
        if (campaign != null) {
            campaignRepository.delete(campaign);
        }
    }

    @Override
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    @Override
    public List<Reward> getAvailableRewardsByCampaignId(Long campaignId) {
        try {
            Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
            return new ArrayList<>(campaign.getRewards());
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Member> getMembersByCampaignId(Long campaignId) {
        try {
            Campaign campaign = campaignRepository.findById(campaignId).orElseThrow();
            return new ArrayList<>(campaign.getMembers());
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }
}
