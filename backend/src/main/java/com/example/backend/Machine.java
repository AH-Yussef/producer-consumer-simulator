package com.example.backend;

import java.util.Random;
import java.util.TimerTask;

import com.example.BlockingQueue;


public class Machine extends TimerTask{
    private int[] fromQueues;
	private int toQueue;
	private int ID;
	private BlockingQueue<Product> product = new BlockingQueue<Product>(1);
    private String color = "187,143,206";
    //processTime is a random integer between 1 and 6 seconds (can be modified in the future) 
	private int processTime = 0;
    //At initialization Machine takes her from and to Queues
	public Machine(int[] fromQueues,int toQueue, int ID){
		this.ID = ID;
	    this.fromQueues=fromQueues;
		this.toQueue=toQueue;
		this.processTime = (2+new Random().nextInt(6))*1000;
	}
	
    //Tries to send the product to all receiver queues (the first empty will get it) Note:needs furthure test
	public void sendProduct(Queue queue){
		try {
			queue.receiveProduct(product.take());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    //Tries to receive the product from all sender queues (the first non-emprt will send it) Note:needs furthure test
	public void receiveProduct(Queue queue) {
		try {
			this.product.put(queue.sendProduct());
			setColor(this.product.peek().getColor());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getProcessTime() {
		// System.out.println("process time = " + this.processTime);
		return processTime;
	}
	public void setProcessTime() {
		this.processTime = (1+new Random().nextInt(6))*1000;
	}
	// getter and setter
	public void setID(int ID){
		this.ID = ID;
	}
	public int getID(){
		return this.ID;
	}
    /*
    the method that runs with each timer
    if the Machine is empty it just takes a new product from the queue
    if it already have a product and it's done with it sends it to the queues and requests another one immendiatly..
    */
	@Override
	public void run() {
		if(product.size()==0) {
			for(int i=0;i<fromQueues.length;i++) {
				if(product.size()==1)break;
				receiveProduct(Simulator.getInstance().getQueue(fromQueues[i]));
			}
		}
		else {
			sendProduct(Simulator.getInstance().getQueue(toQueue));
			setColor("187,143,206");
			for(int i=0;i<fromQueues.length;i++) {
				if(product.size()==1)break;
				receiveProduct(Simulator.getInstance().getQueue(fromQueues[i]));
			}
		}
	}
}
