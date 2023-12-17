package com.example.Loyalty.services.implementations;


import com.example.Loyalty.dtos.LevelDTO;
import com.example.Loyalty.mappers.LevelMapperImpl;
import com.example.Loyalty.models.Level;
import com.example.Loyalty.repositories.LevelRepository;
import com.example.Loyalty.services.LevelService;
import com.example.Loyalty.services.MemberService;
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
public class LevelServiceImpl implements LevelService {
    private final LevelRepository levelRepository;
    private final LevelMapperImpl levelMapper;
    private final MemberService memberService;

    @Override
    public LevelDTO getById(Long id) {
        Level level = levelRepository.findById(id).orElse(null);
        return level != null ? levelMapper.fromLevel(level) : null;
    }
    @Override
    public void updateCustomerCounts() {
        List<Level> levels = levelRepository.findAll();
        for (Level level : levels) {
            int customerCount = memberService.countMembersByLevel(level.getId());
            level.setCustomerCount(customerCount);
            levelRepository.save(level);
        }
    }

    @Override
    public List<LevelDTO> getAllLevels() {
        List<Level> levels = levelRepository.findAll();
        return levels.stream().map(levelMapper::fromLevel).collect(Collectors.toList());
    }
//@Override
//public List<LevelDTO> getAllLevels() {
//    List<Level> levels = levelRepository.findAll();
//    return levels.stream()
//            .map(level -> {
//                LevelDTO levelDTO = levelMapper.fromLevel(level);
//                int customerCount = memberService.countMembersByLevel(level.getId());
//                levelDTO.setCustomerCount(customerCount);
//                return levelDTO;
//            })
//            .collect(Collectors.toList());
//}
    @Override
    public LevelDTO saveLevel(LevelDTO levelDTO) {
        Level level = levelMapper.fromLevelDTO(levelDTO);
        Level savedLevel = levelRepository.save(level);
        return levelMapper.fromLevel(savedLevel);
    }

    @Override
    public Boolean deleteLevel(Long id) {
        levelRepository.deleteById(id);
        return true;
    }

    @Override
    public LevelDTO updateLevel(LevelDTO levelDTO) {
        Level existingLevel = levelRepository.findById(levelDTO.getId()).orElse(null);

        if (existingLevel != null) {
            Level updatedLevel = levelMapper.fromLevelDTO(levelDTO);
            updatedLevel = levelRepository.save(updatedLevel);
            return levelMapper.fromLevel(updatedLevel);
        } else {
            throw new NoSuchElementException("Level not found.");
        }
    }
}
