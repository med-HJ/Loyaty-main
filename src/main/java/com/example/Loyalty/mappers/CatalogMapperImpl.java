package com.example.Loyalty.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogMapperImpl {
    @Bean
    @Qualifier("catalogmodelMapper")
    public ModelMapper catalogmodelMapper() {
        return new ModelMapper();
    }
}
