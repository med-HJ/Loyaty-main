package com.example.Loyalty.services.implementations;
import com.example.Loyalty.dtos.CampaignDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.exceptions.NotFoundException;
import com.example.Loyalty.models.Level;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.dtos.RewardDTO;


import com.example.Loyalty.repositories.LevelRepository;
import com.example.Loyalty.repositories.RewardRepository;
import com.example.Loyalty.services.RewardService;

import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class RewardServiceImpl implements RewardService {

    private RewardRepository rewardRepository;
    private LevelRepository levelRepository;

    private ModelMapper modelMapper;
    @Override
    public RewardDTO getRewardById(Long rewardId) {
        Reward reward = rewardRepository.findById(rewardId)
                .orElseThrow(NoSuchElementException::new);
        return modelMapper.map(reward, RewardDTO.class);
    }
    @Override
    public RewardDTO createReward(RewardDTO rewardDTO, Long levelId) {
        Level level = levelRepository.findById(levelId).orElse(null);

        if (level == null) {
            throw new NotFoundException("Niveau minimum non trouvé avec l'ID : " + levelId);
        }

        // Créez le reward à partir de DTO
        Reward reward = modelMapper.map(rewardDTO, Reward.class);

        // Définissez le niveau minimum requis pour le reward
        reward.setLevel(level);

    // Initialisez le stock
        reward.setStock(rewardDTO.getStock());

        // Enregistrez la récompense dans la base de données
        Reward createdReward = rewardRepository.save(reward);

        // Mappez la récompense créée dans un DTO et retournez-la
        return modelMapper.map(createdReward, RewardDTO.class);
    }
    @Override
    public RewardDTO updateReward(Long rewardId, RewardDTO rewardDTO, Long levelId) {
        // Vérifiez si la récompense avec l'ID spécifié existe
        Reward existingReward = rewardRepository.findById(rewardId).orElse(null);

        if (existingReward != null) {
            // Mettez à jour les propriétés de la récompense existante avec les nouvelles valeurs
            existingReward.setName(rewardDTO.getName());
            existingReward.setDescription(rewardDTO.getDescription());
            existingReward.setPointsRequired(rewardDTO.getPointsRequired());
            existingReward.setStock(rewardDTO.getStock());
            existingReward.setExpiryDate(rewardDTO.getExpiryDate());

            // Mise à jour du niveau minimum requis
            Level level = levelRepository.findById(levelId).orElse(null);
            existingReward.setLevel(level);

            // Enregistrez les modifications dans la base de données
            Reward updatedReward = rewardRepository.save(existingReward);

            return modelMapper.map(updatedReward, RewardDTO.class);
        } else {
            // Si la récompense n'existe pas, vous pouvez lever une exception NotFound
            throw new NotFoundException("Récompense non trouvée avec l'ID : " + rewardId);
        }
    }
    @Override
    public void deleteReward(Long RewardId) {
        Reward reward = rewardRepository.findById(RewardId).orElse(null);
        //supprimer le  reward
        rewardRepository.delete(reward);
    }
    @Override
    public List<RewardDTO> getAllRewards() {
        List<Reward> rewards = rewardRepository.findAll();
        return rewards.stream()
                .map(reward -> modelMapper.map(reward, RewardDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public List<CampaignDTO> getCampaignsByRewardId(Long RewardId) {
        Reward reward = rewardRepository.findById(RewardId).orElse(null);
        List<Campaign> campaigns = reward.getCampaigns();
        return campaigns.stream()
                .map(campaign -> modelMapper.map(campaign, CampaignDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public List<MemberDTO> getMembersByRewardId(Long RewardId) {
        Reward reward = rewardRepository.findById(RewardId).orElse(null);
        List<Member> members = reward.getMembers();
        return members.stream()
                .map(member -> modelMapper.map(member, MemberDTO.class))
                .collect(Collectors.toList());
    }


    }

