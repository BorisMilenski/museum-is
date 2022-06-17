package com.borismilenski.museumis.api;

import com.borismilenski.museumis.model.Position;
import com.borismilenski.museumis.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/positions")
public class PositionController {
    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public List<Position> getAllPositions(){
        return positionService.findAll();
    }

    @PostMapping
    public void addNewPosition(@RequestBody @Valid Position position){
        positionService.create(position);
    }

}
