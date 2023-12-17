package com.example.Loyalty.services;


import com.example.Loyalty.models.Level;

import java.util.List;

public interface LevelService {
    Level getById(Long id);
    List<Level> getAllLevels();
    Level saveLevel(Level level);
    Boolean deleteLevel(Long id);
    Level updateLevel(Level level);

    void updateCustomerCounts();
}
