package com.example.Loyalty.services;



import com.example.Loyalty.models.Benefit;

import java.util.List;

public interface BenefitService {
    Benefit getById(Long id);
    List<Benefit> getAllBenefits();
    Benefit saveBenefit(Benefit benefit);
    Boolean deleteBenefit(Long id);
    List<Benefit> getBenefitsByLevelId(Long levelId);

    Benefit updateBenefit(Benefit benefit);
}
