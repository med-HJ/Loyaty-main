package com.example.Loyalty.services.implementations;

import com.example.Loyalty.dtos.CampaignDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.repositories.CampaignRepository;
import com.example.Loyalty.services.CampaignService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private CampaignRepository campaignRepository;

    private ModelMapper modelMapper;

    @Override
    public CampaignDTO getCampaignById(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
        return modelMapper.map(campaign, CampaignDTO.class);
    }

    @Override
    public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
        Campaign campaign = modelMapper.map(campaignDTO, Campaign.class);
        Campaign createdCampaign = campaignRepository.save(campaign);
        return modelMapper.map(createdCampaign, CampaignDTO.class);
    }

    @Override
    public CampaignDTO updateCampaign(Long campaignId, CampaignDTO campaignDTO) {
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElse(null);
        // Mise à jour des propriétés de la campagne existante avec les nouvelles valeurs du DTO
        existingCampaign.setName(campaignDTO.getName());
        existingCampaign.setStartDate(campaignDTO.getStartDate());
        existingCampaign.setEndDate(campaignDTO.getEndDate());
        existingCampaign.setDescription(campaignDTO.getDescription());


        Campaign updatedCampaign = campaignRepository.save(existingCampaign);
        return modelMapper.map(updatedCampaign, CampaignDTO.class);
    }

    @Override
    public void deletecampaign(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
        // Supprimer la campagne
        campaignRepository.delete(campaign);
    }

    @Override
    public List<CampaignDTO> getAllCampaigns() {
        List<Campaign> campaigns = campaignRepository.findAll();
        return campaigns.stream()
                .map(campaign -> modelMapper.map(campaign, CampaignDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RewardDTO> getAvailableRewardsByCampaignId(Long campaignId) {
        try {
            Campaign campaign = campaignRepository.findById(campaignId).get();
            List<Reward> rewards = campaign.getRewards();
            return rewards.stream()
                    .map(reward -> modelMapper.map(reward, RewardDTO.class))
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            return new ArrayList<>(); // Retourne une liste vide si la campagne n'est pas trouvée
        }
    }

    @Override
    public List<MemberDTO> getMembersByCampaignId(Long campaignId) {
        try {
            Campaign campaign = campaignRepository.findById(campaignId).get();
            List<Member> members = campaign.getMembers();
            return members.stream()
                    .map(member -> modelMapper.map(member, MemberDTO.class))
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            return new ArrayList<>(); // Retourne une liste vide si la campagne n'est pas trouvée
        }
    }
}