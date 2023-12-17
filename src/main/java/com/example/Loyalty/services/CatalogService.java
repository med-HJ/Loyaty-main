package com.example.Loyalty.services;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.models.Catalog;

import java.util.List;

public interface CatalogService {
    Catalog getCatalogById(Long catalogId);
    Catalog createCatalog(Catalog catalog);
    Catalog updateCatalog(Long CatalogId , Catalog catalog);
    void deleteCatalog(Long catalogId);
    List<Catalog> getAllCatalogs();
    List<Reward> getRewardsByCatalogId(Long catalogId);
}
