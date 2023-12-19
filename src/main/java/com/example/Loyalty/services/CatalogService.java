package com.example.Loyalty.services;

import com.example.Loyalty.models.Catalog;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.repositories.CatalogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class CatalogService {

    private CatalogRepository catalogRepository;



    public Catalog getCatalogById(Long catalogId) {
        return catalogRepository.findById(catalogId).orElse(null);
    }


    public Catalog createCatalog(Catalog catalog) {
        return catalogRepository.save(catalog);
    }


    public Catalog updateCatalog(Long catalogId, Catalog updatedCatalog) {
        Catalog existingCatalog = catalogRepository.findById(catalogId).orElse(null);

        if (existingCatalog != null) {
            existingCatalog.setId(updatedCatalog.getId());
            existingCatalog.setDescription(updatedCatalog.getDescription());

            return catalogRepository.save(existingCatalog);
        } else {
            throw new NoSuchElementException("Catalog not found.");
        }
    }


    public void deleteCatalog(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId).orElse(null);
        if (catalog != null) {
            catalogRepository.delete(catalog);
        }
    }


    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }


    public List<Reward> getRewardsByCatalogId(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId).orElse(null);

        if (catalog != null) {
            return catalog.getRewards();
        } else {
            throw new NoSuchElementException("Catalog not found.");
        }
    }
}
