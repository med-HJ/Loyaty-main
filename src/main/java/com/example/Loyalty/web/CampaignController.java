package com.example.Loyalty.web;

import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;

import com.example.Loyalty.services.CampaignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loyalty/campaigns")
@Api(tags = "Campaigns", description = "API for Campaign operations")
public class CampaignController {

    @Autowired
    CampaignService campaignService;

    @GetMapping("/get/{campaignId}")
    @ApiOperation("Get a campaign by its ID")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable("campaignId") Long campaignId) {
        Campaign campaign = campaignService.getCampaignById(campaignId);
        return ResponseEntity.ok(campaign);
    }

    @PostMapping("/create")
    @ApiOperation("Create a new campaign")
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        Campaign createdCampaign = campaignService.createCampaign(campaign);
        return ResponseEntity.ok(createdCampaign);
    }

    @PutMapping("/update/{campaignId}")
    @ApiOperation("Update an existing campaign")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable Long campaignId, @RequestBody Campaign campaign) {
        Campaign updatedCampaign = campaignService.updateCampaign(campaignId, campaign);
        return ResponseEntity.ok(updatedCampaign);
    }

    @DeleteMapping("/delete/{campaignId}")
    @ApiOperation("Delete a campaign by its ID")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long campaignId) {
        campaignService.deletecampaign(campaignId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allcampaigns")
    @ApiOperation("Get all campaigns")
    public ResponseEntity<List<Campaign>> getAllCampaigns() {
        List<Campaign> campaigns = campaignService.getAllCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/rewards/{campaignId}")
    @ApiOperation("Get AvailableRewards by campaign ID")
    public ResponseEntity<List<Reward>> getAvailableRewardsByCampaignId(@PathVariable Long campaignId) {
        List<Reward> rewards = campaignService.getAvailableRewardsByCampaignId(campaignId);
        return ResponseEntity.ok(rewards);
    }

    @GetMapping("/members/{campaignId}")
    @ApiOperation("Get members by campaign ID")
    public ResponseEntity<List<Member>> getMembersByCampaignId(@PathVariable Long campaignId) {
        List<Member> members = campaignService.getMembersByCampaignId(campaignId);
        return ResponseEntity.ok(members);
    }
}
