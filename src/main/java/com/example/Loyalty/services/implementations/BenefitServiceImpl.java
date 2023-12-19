package com.example.Loyalty.services.implementations;

import com.example.Loyalty.models.Benefit;
import com.example.Loyalty.repositories.BenefitRepository;
import com.example.Loyalty.services.BenefitService;
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
public class BenefitServiceImpl implements BenefitService {
    private final BenefitRepository benefitRepository;

    @Override
    public Benefit getById(Long id) {
        return benefitRepository.findById(id).orElse(null);
    }

    @Override
    public List<Benefit> getAllBenefits() {
        return benefitRepository.findAll();
    }

    @Override
    public Benefit saveBenefit(Benefit benefit) {
        return benefitRepository.save(benefit);
    }

    @Override
    public Boolean deleteBenefit(Long id) {
        benefitRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Benefit> getBenefitsByLevelId(Long levelId) {
        return benefitRepository.findByLevelId(levelId);
    }

    @Override
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
