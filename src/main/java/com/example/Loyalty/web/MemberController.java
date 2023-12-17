package com.example.Loyalty.web;


import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;
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
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.getById(id);
        if (member != null) {
            return new ResponseEntity<>(member, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }
    @PostMapping("/enroll")
    public ResponseEntity<Member> saveMember(@RequestBody Member member) {
        Member savedMember = memberService.saveMember(member);
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>("{\"error\" : \"id is required\"}", HttpStatus.BAD_REQUEST);
        }
        Boolean returnValue = memberService.deleteMember(id);
        if (returnValue == null) {
            return new ResponseEntity<>("{\"error\" : \"member not found or there is an error\"}", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("{\"deleted\" : true}", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody Member updatedMember) {
        try {
            Member existingMember = memberService.getById(id);

            if (existingMember != null) {
                Long originalLevelId = existingMember.getLevel().getId();

                if (updatedMember.getFirstName() != null) {
                    existingMember.setFirstName(updatedMember.getFirstName());
                }
                if (updatedMember.getLastName() != null) {
                    existingMember.setLastName(updatedMember.getLastName());
                }

                Member updatedMemberEntity = memberService.updateMember(existingMember);
                updatedMemberEntity.getLevel().setId(originalLevelId);

                return new ResponseEntity<>(updatedMemberEntity, HttpStatus.OK);
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
    public ResponseEntity<List<Reward>> getRedeemHistory(@PathVariable Long memberId) {
        try {
            List<Reward> redemptionHistory = memberService.getRedemptionHistoryByMemberId(memberId);
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
