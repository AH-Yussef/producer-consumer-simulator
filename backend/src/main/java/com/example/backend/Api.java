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

    @GetMapping("/getCurrentImage")
    public String getCurrentImage() {
        return Simulator.getInstance().getAllMachines();
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
