package com.example.Loyalty.mappers;

import com.example.Loyalty.models.Campaign;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CampaignMapperImpl{
        @Bean
        @Qualifier("campaignModelMapper")
        @Primary
        public ModelMapper campaignModelMapper() {
            return new ModelMapper();
        }
}
