package com.example.Loyalty.services;

import com.example.Loyalty.dtos.BenefitDTO;
import com.example.Loyalty.dtos.LevelDTO;

import java.util.List;

public interface BenefitService {
    BenefitDTO getById(Long id);
    List<BenefitDTO> getAllBenefits();
    BenefitDTO saveBenefit(BenefitDTO benefit);
    Boolean deleteBenefit(Long id);
    List<BenefitDTO> getBenefitsByLevelId(Long levelId);

    BenefitDTO updateBenefit(BenefitDTO benefitDTO);
}
