package com.example.Loyalty.repositories;

import com.example.Loyalty.models.Action;
import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findAll() ;
    Optional<Reward> findById(Long rewardId) ;
    Reward save(Reward reward);
    void delete(Reward reward);
    void deleteById(Long id) ;



}
