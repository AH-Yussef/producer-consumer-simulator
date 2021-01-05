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
        ///adding product to queues.. waiting on their implementation for testing
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
}
