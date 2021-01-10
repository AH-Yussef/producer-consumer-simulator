package com.example.backend.componenetsInfo;

public class MachineInfo {
    private int ID;
    private int[] fromQueues;
    private int toQueue;

    public MachineInfo(int ID, int[] fromQueues, int toQueue){
        this.ID = ID;
        this.fromQueues = fromQueues;
        this.toQueue = toQueue;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int[] getFromQueues() {
        return fromQueues;
    }

    public void setFromQueues(int[] fromQueues) {
        this.fromQueues = fromQueues;
    }

    public int getToQueue() {
        return toQueue;
    }

    public void setToQueue(int toQueue) {
        this.toQueue = toQueue;
    }
}
