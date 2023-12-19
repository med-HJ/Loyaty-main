package com.example.Loyalty.services;

import com.example.Loyalty.exceptions.NotFoundException;
import com.example.Loyalty.models.Level;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Campaign;

import com.example.Loyalty.repositories.LevelRepository;
import com.example.Loyalty.repositories.RewardRepository;

import java.util.List;
import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;
    private final LevelRepository levelRepository;


    public Reward getRewardById(Long rewardId) {
        return rewardRepository.findById(rewardId).orElseThrow(NoSuchElementException::new);
    }


    public Reward createReward(Reward reward, Long levelId) {
        Level level = levelRepository.findById(levelId).orElse(null);

        if (level == null) {
            throw new NotFoundException("Niveau minimum non trouvé avec l'ID : " + levelId);
        }

        // Définissez le niveau minimum requis pour le reward
        reward.setLevel(level);

        // Enregistrez la récompense dans la base de données
        return rewardRepository.save(reward);
    }


    public Reward updateReward(Long rewardId, Reward reward, Long levelId) {
        // Vérifiez si la récompense avec l'ID spécifié existe
        Reward existingReward = rewardRepository.findById(rewardId).orElse(null);

        if (existingReward != null) {
            // Mettez à jour les propriétés de la récompense existante avec les nouvelles valeurs
            existingReward.setName(reward.getName());
            existingReward.setDescription(reward.getDescription());
            existingReward.setPointsRequired(reward.getPointsRequired());
            existingReward.setStock(reward.getStock());
            existingReward.setExpiryDate(reward.getExpiryDate());

            // Mise à jour du niveau minimum requis
            Level level = levelRepository.findById(levelId).orElse(null);
            existingReward.setLevel(level);

            // Enregistrez les modifications dans la base de données
            return rewardRepository.save(existingReward);
        } else {
            // Si la récompense n'existe pas, vous pouvez lever une exception NotFound
            throw new NotFoundException("Récompense non trouvée avec l'ID : " + rewardId);
        }
    }


    public void deleteReward(Long rewardId) {
        rewardRepository.deleteById(rewardId);
    }


    public List<Reward> getAllRewards() {
        return rewardRepository.findAll();
    }


    public List<Campaign> getCampaignsByRewardId(Long rewardId) {
        Reward reward = rewardRepository.findById(rewardId).orElse(null);
        return reward != null ? reward.getCampaigns() : null;
    }


    public List<Member> getMembersByRewardId(Long rewardId) {
        Reward reward = rewardRepository.findById(rewardId).orElse(null);
        return reward != null ? reward.getMembers() : null;
    }
}
