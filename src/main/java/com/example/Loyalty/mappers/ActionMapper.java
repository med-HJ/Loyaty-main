package com.example.Loyalty.mappers;

import com.example.Loyalty.dtos.ActionDTO;
import com.example.Loyalty.models.Action;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ActionMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    public static Action convertToAction(ActionDTO actionDTO){
        return modelMapper.map(actionDTO, Action.class);
    }
    public static ActionDTO convertToActionDTO(Action action){
        return modelMapper.map(action, ActionDTO.class);
    }

}
