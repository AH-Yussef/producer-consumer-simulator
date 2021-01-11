package com.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class BlockingQueue<E> {
private Queue<E> queue;
private int max;
private ReentrantLock lock = new ReentrantLock(true);
private Condition notFullQueue = lock.newCondition();
private Condition notEmptyQueue = lock.newCondition();

public BlockingQueue(int size){
    queue = new LinkedList<>();
    this.max=size;
}

public void put(E e) throws InterruptedException {
    lock.lock();
    try{
        while(queue.size()==max){
            notFullQueue.await();
        }
        queue.add(e);
        notEmptyQueue.signalAll();
    }
    finally{
        lock.unlock();
    }
}
public E take() throws InterruptedException {
    lock.lock();
    try{
        while(queue.size()==0){
            notEmptyQueue.await();
        }
        E item = queue.remove();
        notFullQueue.signalAll();
        return item;
    }
    finally{
        lock.unlock();
    }
}
public int size(){
    return queue.size();
}
public E peek(){
    return queue.peek();
}
    
}