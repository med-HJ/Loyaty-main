package com.example.Loyalty.services;

import com.example.Loyalty.models.Benefit;
import com.example.Loyalty.repositories.BenefitRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BenefitService {
    private final BenefitRepository benefitRepository;


    public Benefit getById(Long id) {
        return benefitRepository.findById(id).orElse(null);
    }


    public List<Benefit> getAllBenefits() {
        return benefitRepository.findAll();
    }


    public Benefit saveBenefit(Benefit benefit) {
        return benefitRepository.save(benefit);
    }


    public Boolean deleteBenefit(Long id) {
        benefitRepository.deleteById(id);
        return true;
    }


    public List<Benefit> getBenefitsByLevelId(Long levelId) {
        return benefitRepository.findByLevelId(levelId);
    }


    public Benefit updateBenefit(Benefit updatedBenefit) {
        Benefit existingBenefit = benefitRepository.findById(updatedBenefit.getId()).orElse(null);

        if (existingBenefit != null) {
            existingBenefit.setName(updatedBenefit.getName());
            existingBenefit.setDescription(updatedBenefit.getDescription());
            existingBenefit.setId(updatedBenefit.getId());
            existingBenefit.setLevel(updatedBenefit.getLevel());

            return benefitRepository.save(existingBenefit);
        } else {
            throw new NoSuchElementException("Benefit not found.");
        }
    }

}
