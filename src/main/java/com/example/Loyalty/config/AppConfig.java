package com.example.Loyalty.config;

import com.example.Loyalty.services.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class AppConfig {
    // ... other configurations ...

    @Autowired
    private LevelService levelService;

    @Scheduled(fixedRate = 60000) // Run every hour (you can adjust the interval)
    public void updateCustomerCounts() {
        levelService.updateCustomerCounts();
    }
}
