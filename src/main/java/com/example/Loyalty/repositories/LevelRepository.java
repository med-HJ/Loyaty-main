package com.example.Loyalty.repositories;

import com.example.Loyalty.models.Action;
import com.example.Loyalty.models.Level;
import com.example.Loyalty.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {
    List<Level> findAll() ;
    Optional<Level> findById(Long id) ;

    Level save(Level level);
    void delete(Level level);
    void deleteById(Long id) ;

    List<Level> findByMinPointsGreaterThan(int minPoints);

    List<Level> findByMinPointsGreaterThanEqual(int totalPoints);

    List<Level> findAllByOrderByMinPointsAsc();
}
