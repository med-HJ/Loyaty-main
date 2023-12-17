package com.example.Loyalty.services.implementations;

import com.example.Loyalty.dtos.MovementDTO;
import com.example.Loyalty.enums.MovementType;
import com.example.Loyalty.mappers.MovementMapper;
import com.example.Loyalty.models.Movement;
import com.example.Loyalty.repositories.MovementRepository;
import com.example.Loyalty.services.MovementService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovementServiceImpl implements MovementService {

    @Autowired
    private MovementRepository movementRepository;
    private MovementMapper movementMapper;
    @Override
    public MovementDTO createMovement(MovementDTO movementDTO) {
        Movement movement= movementMapper.convertToMovement(movementDTO);
        movement = movementRepository.save(movement);
        return movementMapper.convertToMovementDTO(movement);

    }

    @Override
    public MovementDTO updateMovement(Long id, MovementDTO movementDTO) {
        Optional<Movement> isMovementExist= movementRepository.findById(id);
        if(isMovementExist.isPresent()){
            Movement updateMovement= isMovementExist.get();
            updateMovement.setAmount(movementDTO.getAmount());
            updateMovement.setMotif(movementDTO.getMotif());
            updateMovement.setDirection(movementDTO.getDirection());
            return movementMapper.convertToMovementDTO(movementRepository.save(updateMovement));

        }
        else{
            throw new NoSuchElementException("Movement not found whit id: "+ id);
        }
    }

    @Override
    public boolean deleteMovement(Long id) {
        try {
            movementRepository.deleteById(id);
            return true;
        }catch (Exception e)  {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public MovementDTO getMovementById(Long id) {

        Optional<Movement> movementOptional= movementRepository.findById(id);
        if(movementOptional.isPresent()){
            Movement movement= movementOptional.get();
            return movementMapper.convertToMovementDTO(movement);
        }else {
            return null;
        }
    }

    @Override
    public List<MovementDTO> getMovementsByTargetMember(Long memberId) {
        return null;
    }

    @Override
    public List<MovementDTO> getAllMovements() {
        List<Movement> movements = movementRepository.findAll();
        return movements.stream()
                .map(movement -> movementMapper.convertToMovementDTO(movement))
                .collect(Collectors.toList());

    }


    @Override
    public List<MovementDTO> getMovementsByType(MovementType movementType) {
        try{
            List<Movement> movements=movementRepository.findMovementsByDirectionIgnoreCase(movementType);
            List<MovementDTO> movementDTOS= new ArrayList<>();
            for (Movement movement: movements){
                movementDTOS.add(movementMapper.convertToMovementDTO(movement));
            }
            return movementDTOS;

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<MovementDTO> getMovementsByActionId(Long actionId) {
        try{
            List<Movement> movements= movementRepository.findMovementsByActionId(actionId);
            List<MovementDTO> movementDTOS= new ArrayList<>();
            for (Movement movement: movements){
                movementDTOS.add(movementMapper.convertToMovementDTO(movement));
            }
            return  movementDTOS;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    /*
    @Override
    public MovementDTO getMovementByActionId(Long actionId){
        Optional<Movement> isMovementExist= movementRepository.findMovementsByActionId(actionId);
            if(isMovementExist.isPresent()){
                Movement movement= isMovementExist.get();
                return movementMapper.convertToMovementDTO(movement);
            }
            else {
                return null;
            }

    } */



}
