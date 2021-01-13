package com.example.backend;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class BlockingQueue<E> {
    private LinkedList<Integer> readyMachines = new LinkedList<>();
    private Queue<E> queue;
    private ReentrantLock lock = new ReentrantLock(true);
    private Condition WakeMachinesUp = lock.newCondition();

    public BlockingQueue(){
        queue = new LinkedList<>();
    }
    public void addToReadyMachines(int ID){
        lock.lock();
        try{readyMachines.addLast(ID);}
        finally{lock.unlock();}
    }
    public void removeFromReadyMachines(int ID){
        lock.lock();
        try{
            if(readyMachines.contains(ID)){
                readyMachines.remove(readyMachines.indexOf(ID));
        }
    }
        finally{lock.unlock();}
    }
    public void wakeMachineUp(){
        lock.lock();
        try{WakeMachinesUp.signalAll();}
        finally{lock.unlock();}
        
    }
    public void put(E e) throws InterruptedException {
        lock.lock();
        try{
            queue.add(e);
            if(readyMachines.size()>0){
                Simulator.getInstance().getMachine(readyMachines.peekFirst()).notifyProcessingState();
            }
        }
        finally{
            lock.unlock();
        }
    }
    public void PutMachineToSleep(int ID) throws InterruptedException {
        lock.lock();
        try{
            while(readyMachines.contains(ID)){
                WakeMachinesUp.await();
            }
        }
        finally{
            lock.unlock();
        }
    }
    public E send(){
        lock.lock();
        try{
        return queue.remove();
        }
        finally{
        lock.unlock(); 
        }
    }
    public int size(){
        lock.lock();
        try{return queue.size();}
        finally{lock.unlock();}
        
    }
    public E peek(){
        lock.lock();
        try{return queue.peek();}
        finally{lock.unlock();}
        
    }
}