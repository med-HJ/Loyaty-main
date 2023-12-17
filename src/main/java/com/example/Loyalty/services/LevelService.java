package com.example.Loyalty.services;

import com.example.Loyalty.dtos.LevelDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.models.Level;

import java.util.List;

public interface LevelService {
    LevelDTO getById(Long id);
    List<LevelDTO> getAllLevels();
    LevelDTO saveLevel(LevelDTO levelDTO);
    Boolean deleteLevel(Long id);
    LevelDTO updateLevel(LevelDTO levelDTO);

    void updateCustomerCounts();
}
