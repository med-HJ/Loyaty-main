package com.example.Loyalty.repositories;

import com.example.Loyalty.models.Action;
import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Catalog;
import com.example.Loyalty.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findAll() ;
    Optional<Campaign> findById(Long id) ;
    Campaign save(Campaign campaign);
    void delete(Campaign campaign);
    void deleteById(Long id) ;

}
