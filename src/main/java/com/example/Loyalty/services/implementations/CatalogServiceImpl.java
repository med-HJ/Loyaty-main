package com.example.Loyalty.services.implementations;

import com.example.Loyalty.models.Catalog;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.repositories.CatalogRepository;
import com.example.Loyalty.services.CatalogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private CatalogRepository catalogRepository;


    @Override
    public Catalog getCatalogById(Long catalogId) {
        return catalogRepository.findById(catalogId).orElse(null);
    }

    @Override
    public Catalog createCatalog(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    @Override
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

    @Override
    public void deleteCatalog(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId).orElse(null);
        if (catalog != null) {
            catalogRepository.delete(catalog);
        }
    }

    @Override
    public List<Catalog> getAllCatalogs() {
        return catalogRepository.findAll();
    }

    @Override
    public List<Reward> getRewardsByCatalogId(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId).orElse(null);

        if (catalog != null) {
            return catalog.getRewards();
        } else {
            throw new NoSuchElementException("Catalog not found.");
        }
    }
}
