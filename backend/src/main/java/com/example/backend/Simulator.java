package com.example.backend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.backend.componenetsInfo.MachineInfo;
import com.example.backend.componenetsInfo.QueueInfo;
import com.example.backend.memento.MementoCollector;


public class Simulator {
    private boolean isSimulationOver=false;
    private int unfinishedProducts = 0;
    private int numberOfProducts;
    private Queue sourceQueue;
    private int inputRate;
    private ScheduledExecutorService[] machinesTimer;
    private ScheduledExecutorService productTimer;
    private HashMap<Integer, Machine> allMachines = new HashMap<Integer, Machine>();
    private HashMap<Integer, Queue> allQueues = new HashMap<Integer, Queue>();
    private HashSet<String> presentColors = new HashSet<String>();

    private JsonConverter jsonConverter = new JsonConverter();
    MementoCollector mementoCollector = new MementoCollector();
    // Singleton
    private static Simulator instance;
    private Simulator(){}
    public static Simulator getInstance(){
        if(instance == null) instance = new Simulator();
        return instance;
    }

    public void startSimulation(String jsonMachines, String jsonQueues, int numberOfProducts){
        //take the json strings and convert them to array of java objects
        this.numberOfProducts = numberOfProducts;
        this.unfinishedProducts = numberOfProducts;
        Machine[] machinesArray = setupMachines(jsonMachines);
        Queue[] queuesArray = setupQueues(jsonQueues);

        this.machinesTimer = new ScheduledExecutorService[machinesArray.length];

        //put queues present in queuesArray into allQueues
        for(int i=0; i<queuesArray.length; i++){
            int queueID = queuesArray[i].getID();

            //put reference of first queue (q0) in sourceQueue
            if(queueID == 0) this.sourceQueue = queuesArray[i];

            this.allQueues.put(queuesArray[i].getID(), queuesArray[i]);
        }

        //put machines present in machinesArray into allMachines and schedule their timers
        for(int i=0; i<machinesArray.length; i++){
            //start timer of each machine
            machinesTimer[i] = Executors.newSingleThreadScheduledExecutor();
            machinesTimer[i].scheduleWithFixedDelay(machinesArray[i], 0, machinesArray[i].getProcessTime(), TimeUnit.MILLISECONDS);
            
            //add each machine to hashmap
            this.allMachines.put(machinesArray[i].getID(), machinesArray[i]);
        }
        
        //put random value in inputRate in milliseconds
        this.inputRate = (new Random().nextInt(5) + 2) * 1000;
        
        //adding product
        addProduct();
    }

    private String getAllMachines(){
        String jsonMachines = "[";
        for(HashMap.Entry<Integer, Machine> mapElement : allMachines.entrySet()){
            jsonMachines += "\n{\"id\" : " + String.valueOf(mapElement.getKey()) + ",\n";
            jsonMachines += "\"color\" : \"rgb(" + mapElement.getValue().getColor() + ")\"},";
        }
        jsonMachines = jsonMachines.substring(0, jsonMachines.length()-1) + "\n]";
        return jsonMachines;
    }
    private String getAllQueues(){
        String jsonQueues = "[";
        for(HashMap.Entry<Integer, Queue> mapElement : allQueues.entrySet()){
            jsonQueues += "\n{\"id\" : " + String.valueOf(mapElement.getKey()) + ",\n";
            jsonQueues += "\"numberOfProducts\" : "+ mapElement.getValue().getNumberOfProducts() + "},";
        }
        jsonQueues = jsonQueues.substring(0, jsonQueues.length()-1) + "\n]";
        return jsonQueues;
    }

    public String getCircuitInfo(){
        String circuitInfo = "{\n";
        circuitInfo += "\"productsNum\" :" + getNumberOfProducts() + ",\n";
        circuitInfo += "\"machines\" :" + getAllMachines() + ",\n";
        circuitInfo += "\"queues\" : " + getAllQueues() + "\n}";

        //save a memento containing circuit info
        mementoCollector.appendToMementoMap(circuitInfo);

        return circuitInfo;
    } 

    public int getNumberOfProducts(){
        return this.numberOfProducts;
    }
    public boolean getIsSimulationOver(){
        return this.isSimulationOver;
    }
    // resetting the simulator to its original state
    public void reset(){
        if(machinesTimer != null) stopAllThreads(machinesTimer);
        this.allMachines.clear();
        this.allQueues.clear();
        if(productTimer != null) this.productTimer.shutdown();
        this.isSimulationOver = false;
        this.presentColors.clear();
        this.mementoCollector.reset();
    }
    /*
    * addProduct: adds/creates a new product to source queue each (inputRate)
    * keeps running until numberOfProducts reaches 0 then stops the thread
    */
    private void addProduct(){
        this.productTimer = Executors.newSingleThreadScheduledExecutor();
        TimerTask inputProduct = new TimerTask(){
			@Override
			public void run() {
				if(numberOfProducts > 0){
                    sourceQueue.receiveProduct(new Product());
                    numberOfProducts--;
                }
                else{
                    productTimer.shutdown();
                }
			}
        };
        productTimer.scheduleWithFixedDelay(inputProduct, 0, inputRate, TimeUnit.MILLISECONDS);
        
    }

    //checking if color already exists
    public boolean colorUsed(String color){
        return presentColors.contains(color);
    }
    //adds color to the set
    public void addColor(String color){
        this.presentColors.add(color);
    }

    /*
    when a product reaches the ending queue this queue is called
    * when the number of unfinishedProducts inside reaches 0
    * this means the simulation is over and stops all machines threads
    */
	public void addFinishedProduct() {
        this.unfinishedProducts--;
        if(unfinishedProducts == 0){
            stopAllThreads(this.machinesTimer);
            isSimulationOver = true;
        }
    }
    
    // stops all machine threads
    private void stopAllThreads(ScheduledExecutorService[]threads){
        for(ScheduledExecutorService thread : threads) thread.shutdown();
    }

    // getting a particular queue object
    public Queue getQueue(int ID){
        return allQueues.get(ID);
    }

    // getting a particular machine object
    public Machine getMachine(int ID){
        return allMachines.get(ID);
    }



     //testing
     public Machine[] setupMachines(String jsonStr){
	     MachineInfo[] machinesInfo = jsonConverter.jsonToMachineInfo(jsonStr);
	     Machine[] allMachines = new Machine[machinesInfo.length];
	     for(int i = 0; i < machinesInfo.length; i++){
	         allMachines[i] = new Machine(machinesInfo[i].getFromQueues(), machinesInfo[i].getToQueue(), machinesInfo[i].getID());
         }
	     return allMachines;
     }

    public Queue[] setupQueues(String jsonStr){
        QueueInfo[] queuesInfo = jsonConverter.jsonToQueueInfo(jsonStr);
        Queue[] allQueues = new Queue[queuesInfo.length];
        for(int i = 0; i < queuesInfo.length; i++){
            allQueues[i] = new Queue(queuesInfo[i].isEndQueue(), queuesInfo[i].getID());
        }
        return allQueues;
    }


}
