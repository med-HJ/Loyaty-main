package com.example.Loyalty.mappers;

import com.example.Loyalty.dtos.BenefitDTO;
import com.example.Loyalty.models.Benefit;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BenefitMapperImpl {
    public BenefitDTO fromBenefit(Benefit benefit){
        BenefitDTO benefitDTO=new BenefitDTO();
        BeanUtils.copyProperties(benefit,benefitDTO);
        return  benefitDTO;
    }
    public Benefit fromBenefitDTO(BenefitDTO benefitDTO){
        Benefit benefit=new Benefit();
        BeanUtils.copyProperties(benefitDTO,benefit);
        return  benefit;
    }
}
