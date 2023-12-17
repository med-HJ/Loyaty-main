package com.example.Loyalty.web;

import com.example.Loyalty.models.Catalog;
import com.example.Loyalty.models.Reward;
import com.example.Loyalty.services.CatalogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loyalty/catalogs")
@Api(tags = "Catalogs", description = "API for Catalog operations")
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @GetMapping("/get/{catalogId}")
    @ApiOperation("Get a catalog by its ID")
    public ResponseEntity<Catalog> getCatalogById(@PathVariable Long catalogId) {
        Catalog catalog = catalogService.getCatalogById(catalogId);
        return ResponseEntity.ok(catalog);
    }

    @PostMapping("/create")
    @ApiOperation("Create a new catalog")
    public ResponseEntity<Catalog> createCatalog(@RequestBody Catalog catalog) {
        Catalog createdCatalog = catalogService.createCatalog(catalog);
        return ResponseEntity.ok(createdCatalog);
    }

    @PutMapping("/update/{catalogId}")
    @ApiOperation("Update an existing catalog")
    public ResponseEntity<Catalog> updateCatalog(@PathVariable Long catalogId, @RequestBody Catalog catalog) {
        Catalog updatedCatalog = catalogService.updateCatalog(catalogId, catalog);
        return ResponseEntity.ok(updatedCatalog);
    }

    @DeleteMapping("/delete/{catalogId}")
    @ApiOperation("Delete a catalog by its ID")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long catalogId) {
        catalogService.deleteCatalog(catalogId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allcatalogs")
    @ApiOperation("Get all catalogs")
    public ResponseEntity<List<Catalog>> getAllCatalogs() {
        List<Catalog> catalogs = catalogService.getAllCatalogs();
        return ResponseEntity.ok(catalogs);
    }

    @GetMapping("/rewards/{catalogId}")
    @ApiOperation("Get Rewards by catalog ID")
    public ResponseEntity<List<Reward>> getRewardsByCatalogId(@PathVariable Long catalogId) {
        List<Reward> rewards = catalogService.getRewardsByCatalogId(catalogId);
        return ResponseEntity.ok(rewards);
    }
}
