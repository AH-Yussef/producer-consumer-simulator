package com.example.backend;

import java.util.Random;
import java.util.TimerTask;

public class Machine extends TimerTask {
	private int[] fromQueues;
	private int toQueue;
	private int ID;
	public Product product = null;
	private String color = "187,143,206";
	// processTime is a random integer between 1 and 6 seconds (can be modified in
	// the future)
	private int processTime = 0;

	// At initialization Machine takes her from and to Queues
	public Machine(int[] fromQueues, int toQueue, int ID) {
		this.ID = ID;
		this.fromQueues = fromQueues;
		this.toQueue = toQueue;
		this.processTime = (2 + new Random().nextInt(10)) * 1000;
	}

	// Tries to send the product to all receiver queues (the first empty will get
	// it) Note:needs furthure test
	public void sendProduct(Queue queue) {
		try {
			queue.receiveProduct(product);
			product = null;
		} finally {
			setColor("187,143,206");
		}
	}

	// Tries to receive the product from all sender queues (the first non-emprt will
	// send it) Note:needs furthure test
	public void receiveProduct(Queue queue) {
		try {
			Product product = queue.sendProduct(this.ID);
			this.product = product;
		} finally {
			setColor(this.product.getColor());
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

	// getter and setter
	public void setID(int ID) {
		this.ID = ID;
	}

	public int getID() {
		return this.ID;
	}

	public void notifyReadyState() throws InterruptedException {
		try{
			for (int i = 0; i < fromQueues.length; i++) {
				Simulator.getInstance().getQueue(fromQueues[i]).notifyReadyState(ID);
			}
		}
		finally{
			Simulator.getInstance().getQueue(fromQueues[0]).allProducts.PutMachineToSleep(ID);
		}
}

	public void notifyProcessingState() {
		try{
			for (int i = 0; i < fromQueues.length; i++) {
				Simulator.getInstance().getQueue(fromQueues[i]).notifyProcessingState(ID);
			}
		}
		finally{
		Simulator.getInstance().getQueue(fromQueues[0]).allProducts.wakeMachineUp();
	}
	}

	/*
	 * the method that runs with each timer if the Machine is empty it just takes a
	 * new product from the queue if it already have a product and it's done with it
	 * sends it to the queues and requests another one immendiatly..
	 */
	@Override
	public void run() {
		if(product!=null){
			sendProduct(Simulator.getInstance().getQueue(toQueue));
		}
		while (product == null) {
			for (int i = 0; i < fromQueues.length; i++) {
				if (Simulator.getInstance().getQueue(fromQueues[i]).allProducts.size() > 0) {
					receiveProduct(Simulator.getInstance().getQueue(fromQueues[i]));
					return;
				}
			}
			try {
				notifyReadyState();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
 