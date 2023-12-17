package com.example.Loyalty.services.implementations;

import com.example.Loyalty.dtos.ActionDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.dtos.RewardRedemptionDTO;
import com.example.Loyalty.enums.ActionType;
import com.example.Loyalty.mappers.MemberMapperImpl;
import com.example.Loyalty.mappers.RewardMapperImpl;
import com.example.Loyalty.models.Level;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.repositories.LevelRepository;
import com.example.Loyalty.repositories.MemberRepository;
import com.example.Loyalty.repositories.RewardRepository;
import com.example.Loyalty.services.ActionService;
import com.example.Loyalty.services.MemberService;
import com.example.Loyalty.services.RewardService;
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
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapperImpl memberMapper;
    private final RewardService rewardService;
    private final Map<Long, List<RewardRedemptionDTO>> redemptionHistoryMap = new HashMap<>();

    private final RewardRepository rewardRepository;
    private final RewardMapperImpl rewardMapper;
    private final LevelRepository levelRepository;
    private final ActionService actionService;
    @Override
    public MemberDTO getById(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        return member != null ? memberMapper.fromMember(member) : null;
    }

    @Override
    public List<MemberDTO> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(memberMapper::fromMember).collect(Collectors.toList());
    }

@Override
public MemberDTO saveMember(MemberDTO memberDTO) {
    Member member = memberMapper.fromMemberDTO(memberDTO);

    // Check if the member is being enrolled for the first time
    boolean isNewEnrollment = (member.getId() == null);

    if (isNewEnrollment) {
        Level defaultLevel = levelRepository.findById(1L).orElse(null);
        if (defaultLevel == null) {
            throw new NoSuchElementException("Default level not found");
        }

        // Set initial values for first-time enrollment
        member.setTotalPoints(100);
        member.setCurrentPoints(100);
        member.setReferralCode(generateReferralCode());
        member.setJoiningDate(LocalDate.now());
        member.setLevel(defaultLevel);
    } else {
        // Get the current level of the member
        Level currentLevel = levelRepository.findById(memberDTO.getLevelId()).orElse(null);
        if (currentLevel == null) {
            throw new NoSuchElementException("Current level not found");
        }

        // Update member's data without changing the level
        member.setLevel(currentLevel);
    }

    Member savedMember = memberRepository.save(member);
    return memberMapper.fromMember(savedMember);
}

@Override
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

//    public void updateMemberLevel(Long memberId) {
//        MemberDTO memberDTO = getById(memberId);
//        if (memberDTO != null) {
//            Level currentLevel = levelRepository.findById(memberDTO.getLevelId()).orElse(null);
//            System.out.println(currentLevel.getMinPoints());
//            if (currentLevel != null) {
//                if (memberDTO.getTotalPoints() >= currentLevel.getMinPoints()) {
//                    List<Level> higherLevels = levelRepository.findByMinPointsGreaterThan(currentLevel.getMinPoints());
////                    System.out.println(higherLevels.toString());
//                    Level nextLevel = higherLevels.stream()
//                            .min(Comparator.comparing(Level::getMinPoints))
//                            .orElse(null);
//                    System.out.println(nextLevel.getMinPoints());
//                    if (nextLevel != null) {
//                        memberDTO.setLevelId(nextLevel.getId());
//                        saveMember(memberDTO);
//                    }
//                }
//            }
//        }
//    }
//public Level updateMemberLevel(Long memberId) {
//    MemberDTO memberDTO = getById(memberId);
//    if (memberDTO != null) {
//        Level currentLevel = levelRepository.findById(memberDTO.getLevelId()).orElse(null);
//        if (currentLevel != null) {
//            if (memberDTO.getTotalPoints() >= currentLevel.getMinPoints()) {
//                List<Level> higherLevels = levelRepository.findByMinPointsGreaterThan(currentLevel.getMinPoints());
//                Level nextLevel = higherLevels.stream()
//                        .min(Comparator.comparing(Level::getMinPoints))
//                        .orElse(null);
//                return nextLevel;
//            }
//        }
//    }
//    return null;
//}
//public Level updateMemberLevel(Long memberId) {
//    MemberDTO memberDTO = getById(memberId);
//    if (memberDTO != null) {
//        List<Level> higherLevels = levelRepository.findAllByOrderByMinPointsAsc();
//
//        List<Level> eligibleLevels = higherLevels.stream()
//                .filter(level -> level.getMinPoints() <= memberDTO.getTotalPoints())
//                .collect(Collectors.toList());
//
//        Level nextLevel = eligibleLevels.stream()
//                .max(Comparator.comparing(Level::getMinPoints))
//                .orElse(null);
//
//        if (nextLevel != null) {
//            memberDTO.setLevelId(nextLevel.getId());
//            saveMember(memberDTO);
//        }
//
//        return nextLevel;
//    }
//    return null;
//}




    @Override
    public Boolean deleteMember(Long id) {
        try {
            memberRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public MemberDTO updateMember(MemberDTO memberDTO) {
        Member existingMember = memberRepository.findById(memberDTO.getId()).orElse(null);

        if (existingMember != null) {
            Member updatedMember = memberMapper.fromMemberDTO(memberDTO);
            updatedMember = memberRepository.save(updatedMember);
            return memberMapper.fromMember(updatedMember);
        } else {
            // Throw an exception or handle member not found scenario
            throw new NoSuchElementException("Member not found.");
        }
    }

@Override

public void redeemPointsByReward(Long memberId, Long rewardId) {
    MemberDTO memberDTO = getById(memberId);
    Reward reward = rewardRepository.findById(rewardId).orElse(null); // Assuming you have a RewardService
    RewardDTO rewardDTO = rewardMapper.fromReward(reward);
    if (memberDTO != null && rewardDTO != null && rewardDTO.getStock() > 0) {
        if (memberDTO.getTotalPoints() >= rewardDTO.getPointsRequired() && memberDTO.getLevelId() >= rewardDTO.getLevelId()) {
            // Create a BURN action
            ActionDTO actionDTO = new ActionDTO();
            actionDTO.setName("Points Redemption");
            actionDTO.setDate(LocalDateTime.now());
            actionDTO.setDescription("Redeemed points for reward: " + rewardDTO.getName());
            actionDTO.setPoints(rewardDTO.getPointsRequired()); // Negative points for BURN action
            actionDTO.setType(ActionType.BURN);
            actionDTO.setMemberId(memberId);

            actionService.createAction(actionDTO,null); // Assuming you have an ActionService


            // Deduct points from member's balance
            int newTotalPoints = memberDTO.getCurrentPoints() - rewardDTO.getPointsRequired();
            memberDTO.setCurrentPoints(newTotalPoints);

            // Update the reward's availability
            rewardDTO.setStock(reward.getStock() - 1);
            rewardService.updateReward(rewardId, rewardDTO); // Assuming you have an updateReward method in RewardService

            // Save the updated member using the MemberService
            saveMember(memberDTO);

            List<RewardRedemptionDTO> redemptionHistory = redemptionHistoryMap.getOrDefault(memberId, new ArrayList<>());
//
            RewardRedemptionDTO redemptionDTO = new RewardRedemptionDTO();
            redemptionDTO.setRewardId(rewardId);
            redemptionDTO.setRewardName(rewardRepository.findById(rewardId).orElse(null).getName());
            redemptionDTO.setRedemptionDate(LocalDateTime.now());

            redemptionHistory.add(redemptionDTO);
            redemptionHistoryMap.put(memberId, redemptionHistory);
            // Save the updated member again to persist the redemption history
            saveMember(memberDTO);
        } else {
            throw new IllegalArgumentException("Insufficient points or level to redeem this reward.");
        }
    } else {
        throw new NoSuchElementException("Member or reward not found.");
    }
}
    @Override
    public void transferPoints(Long sourceMemberId, Long destinationMemberId, int pointsToTransfer) throws Exception {
        MemberDTO sourceMemberDTO = getById(sourceMemberId);
        MemberDTO destinationMemberDTO = getById(destinationMemberId);

        if (sourceMemberDTO == null || destinationMemberDTO == null) {
            throw new NoSuchElementException("Member not found.");
        }

        if (sourceMemberDTO.getCurrentPoints() < pointsToTransfer) {
            throw new Exception("Insufficient points for transfer.");
        }

        // Deduct points from source member
        sourceMemberDTO.setCurrentPoints(sourceMemberDTO.getCurrentPoints() - pointsToTransfer);
        saveMember(sourceMemberDTO);

        // Add points to destination member

        destinationMemberDTO.setCurrentPoints(destinationMemberDTO.getCurrentPoints() + pointsToTransfer);
        destinationMemberDTO.setTotalPoints(destinationMemberDTO.getTotalPoints() + pointsToTransfer);
        checkAndUpdateLevel(destinationMemberDTO,destinationMemberDTO.getTotalPoints() + pointsToTransfer);
        saveMember(destinationMemberDTO);
        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setName("Points Transfer");
        actionDTO.setDate(LocalDateTime.now());
        actionDTO.setDescription("Transfer points from "+sourceMemberDTO.getLastName()+" To "+destinationMemberDTO.getLastName());
        actionDTO.setPoints(pointsToTransfer); // Negative points for BURN action
        actionDTO.setType(ActionType.TRANSFER);
        actionDTO.setMemberId(sourceMemberId);

        actionService.createAction(actionDTO, destinationMemberId.describeConstable());
    }
    @Override
    public void earnPoints(Long memberId, String activityName, int pointsEarned) {
        MemberDTO memberDTO = getById(memberId);
        if (memberDTO != null) {
            ActionDTO actionDTO = new ActionDTO();
            actionDTO.setName(activityName);
            actionDTO.setDate(LocalDateTime.now());
            actionDTO.setDescription("Earned points through activity: " + activityName);
            actionDTO.setPoints(pointsEarned);
            actionDTO.setType(ActionType.EARN);
            actionDTO.setMemberId(memberId);

            actionService.createAction(actionDTO, memberId.describeConstable());


            // Update the member's total points
            int newTotalPoints = memberDTO.getTotalPoints() + pointsEarned;
            memberDTO.setTotalPoints(newTotalPoints);
            memberDTO.setCurrentPoints(newTotalPoints);

            // Check and update member's level immediately if necessary
            checkAndUpdateLevel(memberDTO,newTotalPoints);
//            Level currentLevel = levelRepository.findById(memberDTO.getLevelId()).orElse(null);
//            if (currentLevel != null) {
//                Level nextLevel = levelRepository.findByMinPointsGreaterThan(currentLevel.getMinPoints())
//                        .stream()
//                        .filter(level -> newTotalPoints >= level.getMinPoints())
//                        .min(Comparator.comparing(Level::getMinPoints))
//                        .orElse(null);
//
//                if (nextLevel != null) {
//                    memberDTO.setLevelId(nextLevel.getId());
//                }
//            }

            // Save the updated member using the MemberService
            saveMember(memberDTO);
        } else {
            // Throw an exception or handle member not found scenario
            throw new NoSuchElementException("Member not found.");
        }
    }

    void checkAndUpdateLevel(MemberDTO memberDTO,int newTotalPoints){
        Level currentLevel = levelRepository.findById(memberDTO.getLevelId()).orElse(null);
        if (currentLevel != null) {
            Level nextLevel = levelRepository.findByMinPointsGreaterThan(currentLevel.getMinPoints())
                    .stream()
                    .filter(level -> newTotalPoints >= level.getMinPoints())
                    .min(Comparator.comparing(Level::getMinPoints))
                    .orElse(null);

            if (nextLevel != null) {
                memberDTO.setLevelId(nextLevel.getId());
            }
        }
    }
    @Override
    public int getPointsBalanceByMemberId(Long memberId) {
        MemberDTO memberDTO = getById(memberId);
        if (memberDTO != null) {
            return memberDTO.getTotalPoints();
        } else {
            throw new NoSuchElementException("Member not found.");
        }
    }

    @Override
    public List<RewardRedemptionDTO> getRedemptionHistoryByMemberId(Long memberId) {
        MemberDTO memberDTO = getById(memberId);
        if (memberDTO != null) {
            List<RewardRedemptionDTO> redemptionHistory = redemptionHistoryMap.getOrDefault(memberId, new ArrayList<>());
            return redemptionHistory;
        } else {
            throw new NoSuchElementException("Member not found.");
        }
    }

}
