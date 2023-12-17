package com.example.Loyalty.web;

import com.example.Loyalty.dtos.LevelDTO;
import com.example.Loyalty.services.LevelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/loyalty/levels")
public class LevelController {

    private final LevelService levelService;


    @GetMapping("/{id}")
    public ResponseEntity<LevelDTO> getLevelById(@PathVariable Long id) {
        LevelDTO levelDTO = levelService.getById(id);
        if (levelDTO != null) {
            return new ResponseEntity<>(levelDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<LevelDTO>> getAllLevels() {
        List<LevelDTO> levels = levelService.getAllLevels();
        return new ResponseEntity<>(levels, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LevelDTO> saveLevel(@RequestBody LevelDTO levelDTO) {
        LevelDTO savedLevel = levelService.saveLevel(levelDTO);
        return new ResponseEntity<>(savedLevel, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        boolean isDeleted = levelService.deleteLevel(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLevel(@PathVariable Long id, @RequestBody LevelDTO levelDTO) {
        levelDTO.setId(id); // Set the id from the path variable to the DTO
        try {
            LevelDTO updatedLevel = levelService.updateLevel(levelDTO);
            return new ResponseEntity<>(updatedLevel, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            String errorMessage = "Level not found.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    // Other endpoints...
}

