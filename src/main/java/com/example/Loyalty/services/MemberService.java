package com.example.Loyalty.services;


import com.example.Loyalty.enums.ActionType;
import com.example.Loyalty.models.Action;
import com.example.Loyalty.models.Level;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.repositories.LevelRepository;
import com.example.Loyalty.repositories.MemberRepository;
import com.example.Loyalty.repositories.RewardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final RewardService rewardService;
    private final Map<Long, List<Reward>> redemptionHistoryMap = new HashMap<>();

    private final RewardRepository rewardRepository;
    private final LevelRepository levelRepository;
    private final ActionService actionService;

    public Member getById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }


    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }


    public Member saveMember(Member member) {
        // Check if the member is being enrolled for the first time
        boolean isNewEnrollment = (member.getId() == null);

        if (isNewEnrollment) {
            Level defaultLevel = levelRepository.findById(1L).orElseThrow(() -> new NoSuchElementException("Default level not found"));

            // Set initial values for first-time enrollment
            member.setTotalPoints(100);
            member.setCurrentPoints(100);
            member.setReferralCode(generateReferralCode());
            member.setJoiningDate(LocalDate.now());
            member.setLevel(defaultLevel);
        } else {
            // Get the current level of the member
            Level currentLevel = levelRepository.findById(member.getLevel().getId()).orElseThrow(() -> new NoSuchElementException("Current level not found"));

            // Update member's data without changing the level
            member.setLevel(currentLevel);
        }

        Member savedMember = memberRepository.save(member);
        return savedMember;
    }


public int countMembersByLevel(Long levelId) {
    return memberRepository.countMembersByLevelId(levelId); // Assuming you have a method in MemberRepository to count by level
}
    private String generateReferralCode() {
        // Define the characters that can be used in the code
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        List<String> codes = memberRepository.findAll().stream()
                .map(Member::getReferralCode)
                .collect(Collectors.toList());

        // Define the length of the code
        int codeLength = 10;

        // Create a StringBuilder to build the code
        StringBuilder codeBuilder = new StringBuilder();

        // Generate random characters to create the code
        do {
            Random random = new Random();
            for (int i = 0; i < codeLength; i++) {
                int randomIndex = random.nextInt(characters.length());
                char randomChar = characters.charAt(randomIndex);
                codeBuilder.append(randomChar);
            }
        }while (codes.contains(codes.toString()));
        codes.add(codeBuilder.toString());
        return codeBuilder.toString();
    }







    public Boolean deleteMember(Long id) {
        try {
            memberRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Member updateMember(Member member) {
        Member existingMember = memberRepository.findById(member.getId()).orElse(null);

        if (existingMember != null) {
            return memberRepository.save(member);
        } else {
            // Throw an exception or handle member not found scenario
            throw new NoSuchElementException("Member not found.");
        }
    }


    public void redeemPointsByReward(Long memberId, Long rewardId) {
        Member member = getById(memberId);
        Reward reward = rewardRepository.findById(rewardId).orElse(null); // Assuming you have a RewardService

        if (member != null && reward != null && reward.getStock() > 0) {
            if (member.getTotalPoints() >= reward.getPointsRequired() && member.getLevel().getId() >= reward.getLevel().getId()) {
                // Create a BURN action
                Action action = new Action();
                action.setName("Points Redemption");
                action.setDate(LocalDateTime.now());
                action.setDescription("Redeemed points for reward: " + reward.getName());
                action.setPoints(-reward.getPointsRequired()); // Negative points for BURN action
                action.setType(ActionType.BURN);
                action.setMember(member);

                actionService.createAction(action, null); // Assuming you have an ActionService

                // Deduct points from member's balance
                int newTotalPoints = member.getCurrentPoints() - reward.getPointsRequired();
                member.setCurrentPoints(newTotalPoints);

                // Update the reward's availability
                reward.setStock(reward.getStock() - 1);
                rewardService.updateReward(rewardId, reward, reward.getLevel().getId());

                // Save the updated member
                saveMember(member);

                // Add redemption history
                List<Reward> redemptionHistory = redemptionHistoryMap.getOrDefault(memberId, new ArrayList<>());

                Reward redemption = new Reward();
                redemption.setId(rewardId);
                redemption.setName(reward.getName());
                redemption.setRedemptionDate(LocalDateTime.now());

                redemptionHistory.add(redemption);
                redemptionHistoryMap.put(memberId, redemptionHistory);


                saveMember(member);
            } else {
                throw new IllegalArgumentException("Insufficient points or level to redeem this reward.");
            }
        } else {
            throw new NoSuchElementException("Member or reward not found.");
        }
    }


    public void transferPoints(Long sourceMemberId, Long destinationMemberId, int pointsToTransfer) throws Exception {
        Member sourceMember = getById(sourceMemberId);
        Member destinationMember = getById(destinationMemberId);

        if (sourceMember == null || destinationMember == null) {
            throw new NoSuchElementException("Member not found.");
        }

        if (sourceMember.getCurrentPoints() < pointsToTransfer) {
            throw new Exception("Insufficient points for transfer.");
        }

        // Deduct points from source member
        sourceMember.setCurrentPoints(sourceMember.getCurrentPoints() - pointsToTransfer);
        saveMember(sourceMember);

        // Add points to destination member
        destinationMember.setCurrentPoints(destinationMember.getCurrentPoints() + pointsToTransfer);
        destinationMember.setTotalPoints(destinationMember.getTotalPoints() + pointsToTransfer);
        checkAndUpdateLevel(destinationMember, destinationMember.getTotalPoints() + pointsToTransfer);
        saveMember(destinationMember);

        Action action = new Action();
        action.setName("Points Transfer");
        action.setDate(LocalDateTime.now());
        action.setDescription("Transfer points from " + sourceMember.getLastName() + " To " + destinationMember.getLastName());
        action.setPoints(pointsToTransfer); // Negative points for BURN action
        action.setType(ActionType.TRANSFER);
        action.setMember(sourceMember);

        actionService.createAction(action, Optional.ofNullable(destinationMember.getId()));
    }


    public void earnPoints(Long memberId, String activityName, int pointsEarned) {
        Member member = getById(memberId);
        if (member != null) {
            Action action = new Action();
            action.setName(activityName);
            action.setDate(LocalDateTime.now());
            action.setDescription("Earned points through activity: " + activityName);
            action.setPoints(pointsEarned);
            action.setType(ActionType.EARN);
            action.setMember(member);

            actionService.createAction(action, Optional.ofNullable(memberId));

            // Update the member's total points
            int newTotalPoints = member.getTotalPoints() + pointsEarned;
            member.setTotalPoints(newTotalPoints);
            member.setCurrentPoints(newTotalPoints);

            // Check and update member's level immediately if necessary
            checkAndUpdateLevel(member, newTotalPoints);

            // Save the updated member using the MemberService
            saveMember(member);
        } else {
            // Throw an exception or handle member not found scenario
            throw new NoSuchElementException("Member not found.");
        }
    }

    void checkAndUpdateLevel(Member member, int newTotalPoints) {
        Level currentLevel = levelRepository.findById(member.getLevel().getId()).orElse(null);
        if (currentLevel != null) {
            Level nextLevel = levelRepository.findByMinPointsGreaterThan(currentLevel.getMinPoints())
                    .stream()
                    .filter(level -> newTotalPoints >= level.getMinPoints())
                    .min(Comparator.comparing(Level::getMinPoints))
                    .orElse(null);

            if (nextLevel != null) {
                member.setLevel(nextLevel);
            }
        }
    }


    public int getPointsBalanceByMemberId(Long memberId) {
        Member member = getById(memberId);
        if (member != null) {
            return member.getTotalPoints();
        } else {
            throw new NoSuchElementException("Member not found.");
        }
    }


    public List<Reward> getRedemptionHistoryByMemberId(Long memberId) {
        Member member = getById(memberId);
        if (member != null) {
            List<Reward> redemptionHistory = redemptionHistoryMap.getOrDefault(memberId, new ArrayList<>());
            return redemptionHistory;
        } else {
            throw new NoSuchElementException("Member not found.");
        }
    }

}
