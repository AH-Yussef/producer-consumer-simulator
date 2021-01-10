package com.example.backend;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.example.backend.componenetsInfo.MachineInfo;
import com.example.backend.componenetsInfo.QueueInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class Simulator {
    private boolean isSimulationOver=false;
    private int unfinishedProducts = 0;
    private int numberOfProducts;
    private Queue sourceQueue;
    private int inputRate;
    private Timer[] machinesTimer;
    private Timer productTimer;
    private HashMap<Integer, Machine> allMachines = new HashMap<Integer, Machine>();
    private HashMap<Integer, Queue> allQueues = new HashMap<Integer, Queue>();

    JsonConverter jsonConverter = new JsonConverter();
    // Singleton
    private static Simulator instance;
    private Simulator(){}
    public static Simulator getInstance(){
        if(instance == null) instance = new Simulator();
        return instance;
    }

    @RequestMapping(
        value = "/startSimulation", 
        method = RequestMethod.POST
    )
    public void startSimulation(@RequestParam String jsonMachines, @RequestParam String jsonQueues, @RequestParam int numberOfProducts){
        //take the json strings and convert them to array of java objects
        this.numberOfProducts = numberOfProducts;
        this.unfinishedProducts = numberOfProducts;
//        Machine[] machinesArray = jsonConverter.jsonArrayToMachineArray(jsonMachines);
        Machine[] machinesArray = setupMachines(jsonMachines);
//        Queue[] queuesArray = jsonConverter.jsonArrayToQueueArray(jsonQueues);
        Queue[] queuesArray = setupQueues(jsonQueues);

        this.machinesTimer = new Timer[machinesArray.length];

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
            machinesTimer[i] = new Timer();
            machinesArray[i].setProcessTime();
            machinesTimer[i].scheduleAtFixedRate(machinesArray[i], 0, machinesArray[i].getProcessTime());

            //add each machine to hashmap
            this.allMachines.put(machinesArray[i].getID(), machinesArray[i]);
        }

        //put random value in inputRate in milliseconds
        this.inputRate = (new Random().nextInt(5) + 1) * 1000;

        //adding product
        addProduct();
    }

    private String getAllMachines(){
        String jsonMachines = "[";
        for(HashMap.Entry<Integer, Machine> mapElement : allMachines.entrySet()){
            jsonMachines += "\n{\"ID\" : " + String.valueOf(mapElement.getKey()) + ",\n";
            jsonMachines += "\"color\" : " + mapElement.getValue().getColor() + "},";
        }
        jsonMachines = jsonMachines.substring(0, jsonMachines.length()-1) + "]";
        //before returning we will add here the MEMENTO to save the current image

        //
        return jsonMachines;
    }

    private int getNumberOfProducts(){
        return this.numberOfProducts;
    }
    private boolean getIsSimulationOver(){
        return this.isSimulationOver;
    }
    /*
    * addProduct: adds/creates a new product to source queue each (inputRate)
    * keeps running until numberOfProducts reaches 0 then stops the thread
    */
    private void addProduct(){
        this.productTimer = new Timer();
        TimerTask inputProduct = new TimerTask(){
			@Override
			public void run() {
				if(numberOfProducts > 0){
                    sourceQueue.receiveProduct(new Product());
                    numberOfProducts--;
                }
                else{
                    productTimer.cancel();
                }
			}
        };
        productTimer.schedule(inputProduct, 0, inputRate);
    }

    /*
    * when a product reaches the ending queue this queue is called
    * when the number of unfinishedProducts inside reaches 0
    * this means the simulation is over and stops all machines threads
    */
	public void addFinishedProduct() {
        this.unfinishedProducts++;
        if(unfinishedProducts == 0){
            stopAllThreads(this.machinesTimer);
            isSimulationOver = true;
        }
    }
    
    // stops all machine threads
    private void stopAllThreads(Timer[]threads){
        for(Timer thread : threads) thread.cancel();
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
    


    /*The method found to divide tasks with timers is given in the example below */
    /*
    public static void main(String[] args) {
		Queues queue1 = new Queues();
		Queues queue2 = new Queues();
		LinkedList<Queues> fromQueue= new LinkedList<Queues>();
		fromQueue.add(queue1);
		LinkedList<Queues> toQueue= new LinkedList<Queues>();
		toQueue.add(queue2);
		Machine machine1 = new Machine(fromQueue,toQueue);
		
	
		
		TimerTask inputRate = new TimerTask() {
			@Override
			public void run() {
				System.out.println("Queue1 size= "+queue1.allProducts.size()+"\nQueue2 size= "+queue2.allProducts.size()+"\nMachine1 contains "+machine1.product.size()+" product");
					queue1.receiveProduct(new Product());
			}
		};
		Timer timer[] = new Timer[2];
		timer[0]=new Timer();
		timer[1]=new Timer();
		timer[0].schedule(inputRate,0,1000);
		timer[1].schedule(machine1,0,machine1.getProcessTime());
		System.out.println(machine1.getProcessTime());
	}
    */
    // public static void main(String[]args){
    //     System.out.println("\"color\" : ");
    // }
}
