package com.example.Loyalty.services;
import com.example.Loyalty.dtos.CatalogDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.models.Catalog;

import java.util.List;

public interface CatalogService {
    CatalogDTO getCatalogById(Long catalogId);
    CatalogDTO createCatalog(CatalogDTO catalog);
    CatalogDTO updateCatalog(Long CatalogId , CatalogDTO catalogDTO);
    void deleteCatalog(Long catalogId);
    List<CatalogDTO> getAllCatalogs();
    List<RewardDTO> getRewardsByCatalogId(Long catalogId);
}
