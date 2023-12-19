package com.example.Loyalty.services;

import com.example.Loyalty.models.Level;
import com.example.Loyalty.repositories.LevelRepository;
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
public class LevelService {
    private final LevelRepository levelRepository;
    private final MemberService memberService;

    public Level getById(Long id) {
        return levelRepository.findById(id).orElse(null);
    }


    public void updateCustomerCounts() {
        List<Level> levels = levelRepository.findAll();
        for (Level level : levels) {
            int customerCount = memberService.countMembersByLevel(level.getId());
            level.setCustomerCount(customerCount);
            levelRepository.save(level);
        }
    }


    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    public Level saveLevel(Level level) {
        return levelRepository.save(level);
    }


    public Boolean deleteLevel(Long id) {
        try {
            levelRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public Level updateLevel(Level updatedLevel) {
        Level existingLevel = levelRepository.findById(updatedLevel.getId()).orElse(null);

        if (existingLevel != null) {
            return levelRepository.save(updatedLevel);
        } else {
            throw new NoSuchElementException("Level not found.");
        }
    }
}
