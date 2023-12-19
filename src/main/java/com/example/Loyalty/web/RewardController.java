package com.example.Loyalty.web;

import com.example.Loyalty.models.Reward;
import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.exceptions.NotFoundException;
import com.example.Loyalty.services.RewardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loyalty/rewards")
@Api(tags = "Rewards", description = "API for Reward operations")
public class RewardController {

    @Autowired
    RewardService rewardService;

    @GetMapping("/get/{rewardId}")
    @ApiOperation("Get a reward by its ID")
    public ResponseEntity<Reward> getRewardById(@PathVariable Long rewardId) {
        Reward reward = rewardService.getRewardById(rewardId);
        return ResponseEntity.ok(reward);
    }

    @PostMapping("/create")
    @ApiOperation("Create a new reward")
    public ResponseEntity<Reward> createReward(
            @RequestBody Reward reward,
            @RequestParam(name = "levelId") Long levelId
    ) {
        if (isRewardValid(reward, levelId)) {
            Reward createdReward = rewardService.createReward(reward, levelId);
            return ResponseEntity.ok(createdReward);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private boolean isRewardValid(Reward reward, Long levelId) {
        // Validate the necessary conditions to create a reward with a required minimum level
        return reward.getStock() >= 0
                && reward.getPointsRequired() > 0
                && reward.getExpiryDate() != null
                && levelId != null;
    }

    @PutMapping("/update/{rewardId}")
    @ApiOperation("Update an existing reward")
    public ResponseEntity<Reward> updateReward(
            @PathVariable Long rewardId,
            @RequestBody Reward reward,
            @RequestParam(name = "levelId") Long levelId
    ) {
        try {
            // Call the service to update the reward
            Reward updatedReward = rewardService.updateReward( rewardId, reward, levelId);
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

    @GetMapping("/allrewards")
    @ApiOperation("Get all rewards")
    public ResponseEntity<List<Reward>> getAllRewards() {
        List<Reward> rewards = rewardService.getAllRewards();
        return ResponseEntity.ok(rewards);
    }


    @GetMapping("/campaigns/{rewardId}")
    @ApiOperation("Get campaigns by Reward ID")
    public ResponseEntity<List<Campaign>> getCampaignsByRewardId(@PathVariable("rewardId") Long rewardId) {
        List<Campaign> campaigns = rewardService.getCampaignsByRewardId(rewardId);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/members/{rewardId}")
    @ApiOperation("Get members by Reward ID")
    public ResponseEntity<List<Member>> getMembersByRewardId(@PathVariable("rewardId") Long rewardId) {
        List<Member> members = rewardService.getMembersByRewardId(rewardId);
        return ResponseEntity.ok(members);
    }
}
