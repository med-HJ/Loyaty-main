package com.example.Loyalty.services.implementations;

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
    private final MemberService memberService;

    @Override
    public Level getById(Long id) {
        return levelRepository.findById(id).orElse(null);
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
    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    @Override
    public Level saveLevel(Level level) {
        return levelRepository.save(level);
    }

    @Override
    public Boolean deleteLevel(Long id) {
        try {
            levelRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Level updateLevel(Level updatedLevel) {
        Level existingLevel = levelRepository.findById(updatedLevel.getId()).orElse(null);

        if (existingLevel != null) {
            return levelRepository.save(updatedLevel);
        } else {
            throw new NoSuchElementException("Level not found.");
        }
    }
}
