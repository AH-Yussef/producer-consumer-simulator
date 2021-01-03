package com.example.backend;

import java.util.HashMap;
import java.util.Random;

public class Simulator {
    private Queue sourceQueue = new Queue();
    private int inputTime;
    private HashMap<Integer, Machine> allMachines = new HashMap<Integer, Machine>();
    private HashMap<Integer, Queue> allQueues = new HashMap<Integer, Queue>();

    JsonConverter jsonConverter = new JsonConverter();

    public Simulator(String jsonMachines, String jsonQueues){
        //take the json strings and convert them to array of java objects
        final Machine[] machinesArray = jsonConverter.jsonArrayToMachineArray(jsonMachines);
        final Queue[] queuesArray = jsonConverter.jsonArrayToQueueArray(jsonQueues);

        //put machines present in machinesArray into allMachines
        for(int i=0; i<machinesArray.length; i++){
            this.allMachines.put(i, machinesArray[i]);
        }

        //put queues present in queuesArray into allQueues
        for(int i=0; i<queuesArray.length; i++){
            this.allQueues.put(i, queuesArray[i]);
        }

        //put first queue (q0) in sourceQueue
        this.sourceQueue = queuesArray[0];

        //put random value in inputTime
        this.inputTime = new Random().nextInt(10);
        if(inputTime == 0) inputTime = 1;
    }
    private void addProduct(){
        //adding product to queues.. waiting on their implementation for testing
    }
    
}
