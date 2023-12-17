package com.example.Loyalty.web;

import com.example.Loyalty.dtos.CampaignDTO;
import com.example.Loyalty.dtos.CatalogDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Catalog;
import com.example.Loyalty.services.CatalogService;
import com.example.Loyalty.repositories.CatalogRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loyalty/catalogs")
@Api(tags = "Catalogs", description = "API for Catalog operations")
public class CatalogController {
    @Autowired
     CatalogService catalogService;
    @Autowired
    @Qualifier("catalogmodelMapper")
    private ModelMapper catalogmodelMapper;
    @GetMapping("/get/{catalogId}")
    @ApiOperation("Get a catalog by its ID")
    public ResponseEntity<CatalogDTO> getCatalogById(@PathVariable Long catalogId) {
        CatalogDTO catalog = catalogmodelMapper.map(catalogService.getCatalogById(catalogId), CatalogDTO.class);
        return ResponseEntity.ok(catalog);
    }
    @PostMapping("create")
    @ApiOperation("Create a new catalog")
    public ResponseEntity<CatalogDTO> createCatalog(@RequestBody CatalogDTO catalogDTO) {
        CatalogDTO createdCatalog = catalogService.createCatalog(catalogDTO);
        return ResponseEntity.ok(createdCatalog);
    }
    @PutMapping("/update/{catalogId}")
    @ApiOperation("Update an existing catalog")
    public ResponseEntity<CatalogDTO> updateCatalog(@PathVariable Long catalogId, @RequestBody CatalogDTO catalogDTO) {
        Catalog catalog = catalogmodelMapper.map(catalogDTO, Catalog.class);
        CatalogDTO updatedCatalog = catalogmodelMapper.map(catalogService.updateCatalog(catalogId, catalogDTO), CatalogDTO.class);
        return ResponseEntity.ok(updatedCatalog);
    }
    @DeleteMapping("/delete/{catalogId}")
    @ApiOperation("Delete a catalog by its ID")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long catalogId) {
        catalogService.deleteCatalog(catalogId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("allcatalogs")
    @ApiOperation("Get all catalogs")
    public ResponseEntity<List<CatalogDTO>> getAllCatalogs() {
        List<CatalogDTO> catalogs = catalogService.getAllCatalogs()
                .stream()
                .map(catalog -> catalogmodelMapper.map(catalog, CatalogDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(catalogs);
    }
    @GetMapping("/rewards/{catalogId}")
    @ApiOperation("Get  Rewards by catalog  ID")
    public ResponseEntity<List<RewardDTO>> getRewardsByCatalogId(@PathVariable("catalogId") Long catalogId) {
        List<RewardDTO> rewards = catalogService.getRewardsByCatalogId(catalogId);
        return ResponseEntity.ok(rewards);

    }

}
