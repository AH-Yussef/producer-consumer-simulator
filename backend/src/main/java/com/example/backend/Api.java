package com.example.backend;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class Api {
    @RequestMapping(
        value = "/startSimulation", 
        method = RequestMethod.POST
    )
    public void startSimulation(@RequestParam String jsonMachines, @RequestParam String jsonQueues, @RequestParam int numberOfProducts){
        Simulator.getInstance().startSimulation(jsonMachines, jsonQueues, numberOfProducts);
    }
}
