package com.example.Loyalty.services.implementations;
import com.example.Loyalty.dtos.CatalogDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Catalog;
import com.example.Loyalty.models.Reward;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.Loyalty.repositories.CatalogRepository;
import com.example.Loyalty.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private CatalogRepository catalogRepository;

    private ModelMapper modelMapper;
    @Override
    public CatalogDTO getCatalogById(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId).orElse(null);
        return modelMapper.map(catalog, CatalogDTO.class);
    }
    @Override
    public CatalogDTO createCatalog(CatalogDTO catalogDTO) {
        Catalog catalog = modelMapper.map(catalogDTO, Catalog.class);
        Catalog createdCatalog = catalogRepository.save(catalog);
        return modelMapper.map(createdCatalog, CatalogDTO.class);
    }
    @Override
    public CatalogDTO updateCatalog(Long catalogId, CatalogDTO catalogDTO) {
        Catalog existingCatalog = catalogRepository.findById(catalogId).orElse(null);

        // Mise à jour des propriétés du catalogue existant avec les nouvelles valeurs du DTO
        existingCatalog.setId(catalogDTO.getId());
        existingCatalog.setDescription(catalogDTO.getDescription());

        Catalog updatedCatalog = catalogRepository.save(existingCatalog);
        return modelMapper.map(updatedCatalog, CatalogDTO.class);
    }
    @Override
    public void deleteCatalog(Long catalogId) {
       Catalog catalog = catalogRepository.findById(catalogId).orElse(null);
       //supprimer le catalog
        catalogRepository.delete(catalog);
    }
    @Override
    public List<CatalogDTO> getAllCatalogs() {
        List<Catalog> catalogs = catalogRepository.findAll();
        return catalogs.stream()
                .map(catalog -> modelMapper.map(catalog, CatalogDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public List<RewardDTO> getRewardsByCatalogId(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId).orElse(null);

        List<Reward> rewards = catalog.getRewards();
        return rewards.stream()
                .map(reward -> modelMapper.map(reward, RewardDTO.class))
                .collect(Collectors.toList());
    }

    }

