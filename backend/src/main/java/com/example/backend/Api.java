package com.example.backend;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class Api {
    @PostMapping("/startSimulation")
    public void startSimulation(@RequestParam String jsonMachines, @RequestParam String jsonQueues, @RequestParam int numberOfProducts){
        Simulator.getInstance().reset();
        Simulator.getInstance().startSimulation(jsonMachines, jsonQueues, numberOfProducts);
    }

    @PostMapping("/resetSimulator")
    public void resetSimulator(){
        Simulator.getInstance().reset();
    }

    @GetMapping("/getCurrentImage")
    public String getCurrentImage() {
        return Simulator.getInstance().getCircuitInfo();
    }

    @GetMapping("/isSimulationFinished")
    public boolean isSimulationFinished() {
        return Simulator.getInstance().getIsSimulationOver();
    }

    @GetMapping("/remainingProducts")
    public int getRemainingProducts() {
        return Simulator.getInstance().getNumberOfProducts();
    }
}
