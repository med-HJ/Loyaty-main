package com.example.Loyalty.services.implementations;

import com.example.Loyalty.dtos.BenefitDTO;
import com.example.Loyalty.mappers.BenefitMapperImpl;
import com.example.Loyalty.models.Benefit;
import com.example.Loyalty.repositories.BenefitRepository;
import com.example.Loyalty.services.BenefitService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BenefitServiceImpl implements BenefitService {
    private final BenefitRepository benefitRepository;
    private final BenefitMapperImpl benefitMapper;
    @Override
    public BenefitDTO getById(Long id) {
        Benefit benefit = benefitRepository.findById(id).orElse(null);
        return benefit != null ? benefitMapper.fromBenefit(benefit) : null;
    }

    @Override
    public List<BenefitDTO> getAllBenefits() {
        List<Benefit> benefits = benefitRepository.findAll();
        return benefits.stream().map(benefitMapper::fromBenefit).collect(Collectors.toList());
    }

    @Override
    public BenefitDTO saveBenefit(BenefitDTO benefitDTO) {
        Benefit benefit = benefitMapper.fromBenefitDTO(benefitDTO);
        Benefit savedBenefit = benefitRepository.save(benefit);
        return benefitMapper.fromBenefit(savedBenefit);
    }

    @Override
    public Boolean deleteBenefit(Long id) {
        benefitRepository.deleteById(id);
        return true;
    }

    @Override
    public List<BenefitDTO> getBenefitsByLevelId(Long levelId) {
        List<Benefit> benefits = benefitRepository.findByLevelId(levelId);
        return benefits.stream().map(benefitMapper::fromBenefit).collect(Collectors.toList());
    }

    @Override
    public BenefitDTO updateBenefit(BenefitDTO benefitDTO) {
        Benefit existingBenefit = benefitRepository.findById(benefitDTO.getId()).orElse(null);

        if (existingBenefit != null) {
            Benefit updatedBenefit = benefitMapper.fromBenefitDTO(benefitDTO);
            updatedBenefit = benefitRepository.save(updatedBenefit);
            return benefitMapper.fromBenefit(updatedBenefit);
        } else {
            throw new NoSuchElementException("Benefit not found.");
        }
    }
}
