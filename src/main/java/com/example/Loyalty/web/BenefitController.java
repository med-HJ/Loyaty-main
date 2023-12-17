package com.example.Loyalty.web;

import com.example.Loyalty.dtos.BenefitDTO;
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
    public ResponseEntity<BenefitDTO> getBenefitById(@PathVariable Long id) {
        BenefitDTO benefitDTO = benefitService.getById(id);
        if (benefitDTO != null) {
            return new ResponseEntity<>(benefitDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<BenefitDTO>> getAllBenefits() {
        List<BenefitDTO> benefits = benefitService.getAllBenefits();
        return new ResponseEntity<>(benefits, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BenefitDTO> saveBenefit(@RequestBody BenefitDTO benefitDTO) {
        BenefitDTO savedBenefit = benefitService.saveBenefit(benefitDTO);
        return new ResponseEntity<>(savedBenefit, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBenefit(@PathVariable Long id) {
        benefitService.deleteBenefit(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/level/{levelId}")
    public ResponseEntity<List<BenefitDTO>> getBenefitsByLevelId(@PathVariable Long levelId) {
        List<BenefitDTO> benefits = benefitService.getBenefitsByLevelId(levelId);
        return new ResponseEntity<>(benefits, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBenefit(@PathVariable Long id, @RequestBody BenefitDTO benefitDTO) {
        benefitDTO.setId(id); // Set the id from the path variable to the DTO
        try {
            BenefitDTO updatedBenefit = benefitService.updateBenefit(benefitDTO);
            return new ResponseEntity<>(updatedBenefit, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            String errorMessage = "Benefit not found.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

}
