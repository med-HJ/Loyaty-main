package com.example.Loyalty.repositories;

import com.example.Loyalty.models.Action;
import com.example.Loyalty.models.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    List<Benefit> findAll() ;
    Optional<Benefit> findById(Long id) ;
    Benefit save(Benefit benefit);
    void delete(Benefit benefit);
    void deleteById(Long id) ;

    List<Benefit> findByLevelId(Long levelId);
}
