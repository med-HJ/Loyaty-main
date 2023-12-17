package com.example.Loyalty.repositories;

import com.example.Loyalty.models.Action;
import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    List<Catalog> findAll() ;
    Optional<Catalog> findById(Long catalogId) ;
    Catalog save(Catalog catalog);
    void delete(Catalog catalog);
    void deleteById(Long id) ;


}
