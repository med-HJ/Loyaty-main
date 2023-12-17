package com.example.Loyalty.web;

import com.example.Loyalty.dtos.CampaignDTO;
import com.example.Loyalty.dtos.CatalogDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.exceptions.NotFoundException;
import com.example.Loyalty.models.Catalog;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.services.CatalogService;
import com.example.Loyalty.services.RewardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loyalty/rewards")
@Api(tags = "Rewards", description = "API for Reward operations")
public class RewardController {
    @Autowired
     RewardService rewardService;
    @Autowired
    private ModelMapper rewardmodelMapper;
    @GetMapping("/get/{rewardId}")
    @ApiOperation("Get a reward by its ID")
    public ResponseEntity<RewardDTO> getRewardById(@PathVariable Long rewardId) {
        RewardDTO reward = rewardmodelMapper.map(rewardService.getRewardById(rewardId), RewardDTO.class);
        return ResponseEntity.ok(reward);
    }
    @PostMapping("create")
    @ApiOperation("Create a new reward")
    public ResponseEntity<RewardDTO> createReward(
            @RequestBody RewardDTO rewardDTO,
            @RequestParam(name = "levelId") Long levelId
    ) {
        if (isRewardValid(rewardDTO, levelId)) {
            RewardDTO createdReward = rewardService.createReward(rewardDTO, levelId);
            return ResponseEntity.ok(createdReward);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    private boolean isRewardValid(RewardDTO rewardDTO, Long levelId) {
        // Validez les conditions nécessaires pour créer une récompense avec un niveau minimum requis
        return rewardDTO.getStock() >= 0
                && rewardDTO.getPointsRequired() > 0
                && rewardDTO.getExpiryDate() != null
                && levelId != null;
    }

    @PutMapping("/update/{rewardId}")
    @ApiOperation("Update an existing reward")
    public ResponseEntity<RewardDTO> updateReward(
            @PathVariable Long rewardId,
            @RequestBody RewardDTO rewardDTO,
            @RequestParam(name = "levelId") Long levelId
    ) {
        try {
            // Appelez le service pour mettre à jour la récompense
            RewardDTO updatedReward = rewardService.updateReward(rewardId, rewardDTO, levelId);
            return ResponseEntity.ok(updatedReward);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/delete/{rewardId}")
    @ApiOperation("Delete a reward by its ID")
    public ResponseEntity<Void> deleteReward(@PathVariable Long rewardId) {
        rewardService.deleteReward(rewardId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("allrewards")
    @ApiOperation("Get all rewards")
    public ResponseEntity<List<RewardDTO>> getAllRewards() {
        List<RewardDTO> rewards = rewardService.getAllRewards()
                .stream()
                .map(reward -> rewardmodelMapper.map(reward, RewardDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(rewards);
    }
    @GetMapping("/campaigns/{rewardId}")
    @ApiOperation("Get campaigns by Reward  ID")
    public ResponseEntity<List<CampaignDTO>> getCampaignsByRewardId(@PathVariable("rewardId") Long rewardId) {
        List<CampaignDTO> campaigns = rewardService.getCampaignsByRewardId(rewardId);
        return ResponseEntity.ok(campaigns);

    }
    @GetMapping("/members/{rewardId}")
    @ApiOperation("Get members by Reward  ID")
    public ResponseEntity<List<MemberDTO>> getMembersByRewardId(@PathVariable("rewardId") Long rewardId) {
        List<MemberDTO> members = rewardService.getMembersByRewardId(rewardId);
        return ResponseEntity.ok(members);

    }
}
