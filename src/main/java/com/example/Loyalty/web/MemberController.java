package com.example.Loyalty.web;

import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardRedemptionDTO;
import com.example.Loyalty.services.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/loyalty/members")
@CrossOrigin("*")
@AllArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        MemberDTO memberDTO = memberService.getById(id);
        if (memberDTO != null) {
            return new ResponseEntity<>(memberDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        List<MemberDTO> members = memberService.getAllMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }
    @PostMapping("/enroll")
    public ResponseEntity<MemberDTO> saveMember(@RequestBody MemberDTO memberDTO) {
        MemberDTO savedMember = memberService.saveMember(memberDTO);
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        if(id == null){
            return new ResponseEntity<>("{\"error\" : \"id is required\"}" , HttpStatus.BAD_REQUEST) ;
        }
        Boolean returnValue = memberService.deleteMember(id);
        if(returnValue == null){
            return new ResponseEntity<>("{\"error\" : \"member not found or there an error\"}" , HttpStatus.BAD_REQUEST) ;
        }
        return new ResponseEntity<>("{\"deleted\" : true}" , HttpStatus.OK) ;
    }
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody MemberDTO memberDTO) {
//        memberDTO.setId(id); // Set the id from the path variable to the DTO
//        try {
//            MemberDTO updatedMember = memberService.updateMember(memberDTO);
//            return new ResponseEntity<>(updatedMember, HttpStatus.OK);
//        } catch (NoSuchElementException e) {
//            // Handle member not found scenario and return a custom error response
//            String errorMessage = "Member not found.";
//            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
//        }
//    }
@PutMapping("/{id}")
public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody MemberDTO updatedMemberDTO) {
    try {
        MemberDTO existingMemberDTO = memberService.getById(id);

        if (existingMemberDTO != null) {
            Long originalLevelId = existingMemberDTO.getLevelId();

            if (updatedMemberDTO.getFirstName() != null) {
                existingMemberDTO.setFirstName(updatedMemberDTO.getFirstName());
            }
            if (updatedMemberDTO.getLastName() != null) {
                existingMemberDTO.setLastName(updatedMemberDTO.getLastName());
            }

            MemberDTO updatedMember = memberService.updateMember(existingMemberDTO);
            updatedMember.setLevelId(originalLevelId);

            return new ResponseEntity<>(updatedMember, HttpStatus.OK);
        } else {
            // Handle member not found scenario and return a custom error response
            String errorMessage = "Member not found.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    } catch (NoSuchElementException e) {
        // Handle member not found scenario and return a custom error response
        String errorMessage = "Member not found.";
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}

    @PostMapping("/{memberId}/earn")
    public ResponseEntity<String> earnPoints(
            @PathVariable Long memberId,
            @RequestParam String activityName,
            @RequestParam int pointsEarned
    ) {
        try {
            memberService.earnPoints(memberId, activityName, pointsEarned);
            return new ResponseEntity<>("Points earned successfully.", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            String errorMessage = "Member not found.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{memberId}/redeem/{rewardId}")
    public ResponseEntity<String> redeemPointsByReward(
            @PathVariable Long memberId,
            @PathVariable Long rewardId
    ) {
        try {
            memberService.redeemPointsByReward(memberId, rewardId);
            return new ResponseEntity<>("Reward redeemed successfully.", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            String errorMessage = "Member or reward not found.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Insufficient points to redeem this reward.";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{memberId}/points")
    public ResponseEntity<?> getMemberPoints(@PathVariable Long memberId) {
        try {
            int points = memberService.getPointsBalanceByMemberId(memberId);
            return new ResponseEntity<>(points, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            String errorMessage = "Member not yet found";
            return new ResponseEntity<>(errorMessage,HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{memberId}/redeem-history")
    public ResponseEntity<List<RewardRedemptionDTO>> getRedeemHistory(@PathVariable Long memberId) {
        try {
            List<RewardRedemptionDTO> redemptionHistory = memberService.getRedemptionHistoryByMemberId(memberId);
            return new ResponseEntity<>(redemptionHistory, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{sourceMemberId}/transfer/{destinationMemberId}")
    public ResponseEntity<String> transferPoints(
            @PathVariable Long sourceMemberId,
            @PathVariable Long destinationMemberId,
            @RequestParam int pointsToTransfer
    ) {
        try {
            memberService.transferPoints(sourceMemberId, destinationMemberId, pointsToTransfer);
            return new ResponseEntity<>("Points transferred successfully.", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            String errorMessage = "Member not found.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            String errorMessage = "Insufficient points for transfer.";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

}
