package com.example.backend;

import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Machine extends TimerTask{
    private LinkedList<Queue> fromQueue = null;
	private LinkedList<Queue> toQueue = null;
	private BlockingQueue<Product> product = new ArrayBlockingQueue<Product>(1);
    private String color = null;
    //processTime is a random integer between 1 and 6 seconds (can be modified in the future) 
	private int processTime = (1+new Random().nextInt(6))*1000;
    //At initialization Machine takes her from and to Queues
	public Machine(LinkedList<Queue> fromQueue,LinkedList<Queue> toQueue){
	    this.fromQueue=fromQueue;
	    this.toQueue=toQueue;
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
			setColor(this.product.peek().color);
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
		return processTime;
	}
	public void setProcessTime(int processTime) {
		this.processTime = processTime;
    }
    /*
    the method that runs with each timer
    if the Machine is empty it just takes a new product from the queue
    if it already have a product and it's done with it sends it to the queues and takes another one immendiatly
    */
	@Override
	public void run() {
		if(product.size()==0) {
			for(int i=0;i<fromQueue.size();i++) {
				receiveProduct(fromQueue.get(i));
				}
		}
		else {
			for(int i=0;i<toQueue.size();i++) {
				sendProduct(toQueue.get(i));
				}
			for(int i=0;i<fromQueue.size();i++) {
				receiveProduct(fromQueue.get(i));
				}
		}
	}
}
