package com.example.Loyalty.web;

import com.example.Loyalty.models.Level;
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
    public ResponseEntity<Level> getLevelById(@PathVariable Long id) {
        Level level = levelService.getById(id);
        if (level != null) {
            return new ResponseEntity<>(level, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Level>> getAllLevels() {
        List<Level> levels = levelService.getAllLevels();
        return new ResponseEntity<>(levels, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Level> saveLevel(@RequestBody Level level) {
        Level savedLevel = levelService.saveLevel(level);
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
    public ResponseEntity<?> updateLevel(@PathVariable Long id, @RequestBody Level level) {
        level.setId(id); // Set the id from the path variable to the
        try {
            Level updatedLevel = levelService.updateLevel(level);
            return new ResponseEntity<>(updatedLevel, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            String errorMessage = "Level not found.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    // Other endpoints...
}

