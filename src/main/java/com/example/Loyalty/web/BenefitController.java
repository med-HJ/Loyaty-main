package com.example.Loyalty.web;


import com.example.Loyalty.models.Benefit;
import com.example.Loyalty.services.BenefitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/loyalty/benefits")
public class BenefitController {
    private final BenefitService benefitService;
    @GetMapping("/{id}")
    public ResponseEntity<Benefit> getBenefitById(@PathVariable Long id) {
        Benefit benefit = benefitService.getById(id);
        if (benefit != null) {
            return new ResponseEntity<>(benefit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Benefit>> getAllBenefits() {
        List<Benefit> benefits = benefitService.getAllBenefits();
        return new ResponseEntity<>(benefits, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Benefit> saveBenefit(@RequestBody Benefit benefit) {
        Benefit savedBenefit = benefitService.saveBenefit(benefit);
        return new ResponseEntity<>(savedBenefit, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBenefit(@PathVariable Long id) {
        benefitService.deleteBenefit(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/level/{levelId}")
    public ResponseEntity<List<Benefit>> getBenefitsByLevelId(@PathVariable Long levelId) {
        List<Benefit> benefits = benefitService.getBenefitsByLevelId(levelId);
        return new ResponseEntity<>(benefits, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBenefit(@PathVariable Long id, @RequestBody Benefit benefit) {
        benefit.setId(id); // Set the id from the path variable to the
        try {
            Benefit updatedBenefit = benefitService.updateBenefit(benefit);
            return new ResponseEntity<>(updatedBenefit, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            String errorMessage = "Benefit not found.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

}
