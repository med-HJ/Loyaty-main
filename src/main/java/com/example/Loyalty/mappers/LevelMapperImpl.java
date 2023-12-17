package com.example.Loyalty.mappers;

import com.example.Loyalty.dtos.LevelDTO;
import com.example.Loyalty.models.Level;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service

public class LevelMapperImpl {
    public LevelDTO fromLevel(Level level){
        LevelDTO levelDTO=new LevelDTO();
        BeanUtils.copyProperties(level,levelDTO);
        return  levelDTO;
    }
    public Level fromLevelDTO(LevelDTO levelDTO){
        Level level=new Level();
        BeanUtils.copyProperties(levelDTO,level);
        return  level;
    }
}
