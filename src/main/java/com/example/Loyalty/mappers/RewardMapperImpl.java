package com.example.Loyalty.mappers;

import com.example.Loyalty.dtos.LevelDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.models.Level;
import com.example.Loyalty.models.Reward;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
public class RewardMapperImpl {
    @Bean
    @Qualifier("rewardmodelMapper")
    public ModelMapper rewardmodelMapper() {
        return new ModelMapper();
    }
    public RewardDTO fromReward(Reward reward){
        RewardDTO rewardDTO=new RewardDTO();
        BeanUtils.copyProperties(reward,rewardDTO);
        return  rewardDTO;
    }
    public Reward fromRewardDTO(RewardDTO rewardDTO){
        Reward reward=new Reward();
        BeanUtils.copyProperties(rewardDTO,reward);
        return  reward;
    }
}
