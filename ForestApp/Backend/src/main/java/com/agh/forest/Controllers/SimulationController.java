package com.agh.forest.Controllers;

import com.agh.forest.Services.SimulationService;
import com.agh.forest.dto.ForestPixelDto;
import com.agh.forest.dto.Summary;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Executors;

@RestController
@CrossOrigin("*")
@AllArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("/start")
    public ResponseEntity<Boolean> startSimulation() {
        simulationService.startSimulation();
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/end")
    public ResponseEntity<Summary> endSimiulation() {

        return new ResponseEntity<>(simulationService.endSimulation(), HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Boolean> newSimulation() {
        simulationService.getForestSimulationApp().getAgentDashboard().resetAgents();
        simulationService.getForestSimulationApp().createBoard("London");
        simulationService.getForestSimulationApp().setScheduledExecutorService(Executors.newScheduledThreadPool(26));
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
