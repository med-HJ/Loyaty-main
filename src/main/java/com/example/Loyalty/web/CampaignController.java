package com.example.Loyalty.web;

import com.example.Loyalty.dtos.CampaignDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.services.CampaignService;

import org.modelmapper.ModelMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loyalty/campaigns")
@Api(tags = "Campaigns", description = "API for Campaign operations")
public class CampaignController {
    @Autowired
     CampaignService campaignService;

    @Autowired
    @Qualifier("campaignModelMapper")
    private ModelMapper campaignmodelMapper;
    @GetMapping("/get/{campaignId}")
    @ApiOperation("Get a campaign by its ID")
    public ResponseEntity<CampaignDTO> getCampaignById(@PathVariable("campaignId") Long campaignId) {
        CampaignDTO campaign = campaignmodelMapper.map(campaignService.getCampaignById(campaignId), CampaignDTO.class);
        return ResponseEntity.ok(campaign);
    }
    @PostMapping("/create")
    @ApiOperation("Create a new campaign")
    public ResponseEntity<CampaignDTO> createCampaign(@RequestBody CampaignDTO campaignDTO) {
        CampaignDTO createdCampaign = campaignService.createCampaign(campaignDTO);
        return ResponseEntity.ok(createdCampaign);
    }
    @PutMapping("/update/{campaignId}")
    @ApiOperation("Update an existing campaign")
    public ResponseEntity<CampaignDTO> updateCampaign(@PathVariable Long campaignId, @RequestBody CampaignDTO campaignDTO) {
        Campaign campaign = campaignmodelMapper.map(campaignDTO, Campaign.class);
        CampaignDTO updatedCampaign = campaignmodelMapper.map(campaignService.updateCampaign(campaignId, campaignDTO), CampaignDTO.class);
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
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns() {
        List<CampaignDTO> campaigns = campaignService.getAllCampaigns()
                .stream()
                .map(campaign -> campaignmodelMapper.map(campaign, CampaignDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(campaigns);
    }
    @GetMapping("/rewards/{campaignId}")
    @ApiOperation("Get  AvailableRewards by campaign  ID")
    public ResponseEntity<List<RewardDTO>> getAvailableRewardsByCampaignId(@PathVariable Long campaignId) {
        List<RewardDTO> rewards = campaignService.getAvailableRewardsByCampaignId(campaignId);
        return ResponseEntity.ok(rewards);
    }
    @GetMapping("/members/{campaignId}")
    @ApiOperation("Get  members by campaign  ID")
    public ResponseEntity<List<MemberDTO>> getMembersByCampaignId(@PathVariable Long campaignId) {
        List<MemberDTO> members = campaignService.getMembersByCampaignId(campaignId);
        return ResponseEntity.ok(members);
    }

}
