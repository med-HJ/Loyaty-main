package com.example.Loyalty.mappers;

import com.example.Loyalty.dtos.EventDTO;
import com.example.Loyalty.models.Event;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    private static final ModelMapper modelMapper= new ModelMapper();
    public static Event convertToEvent(EventDTO eventDTO){
        return modelMapper.map(eventDTO, Event.class);
    }
    public static EventDTO convertToEventDTO(Event event){
        return modelMapper.map(event, EventDTO.class);
    }

}

