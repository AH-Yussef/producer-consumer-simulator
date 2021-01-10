package com.example.backend.componenetsInfo;

public class QueueInfo {
    private int ID;
    private boolean isEndQueue;

    public QueueInfo(int ID, boolean isEndQueue){
        this.ID = ID;
        this.isEndQueue = isEndQueue;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isEndQueue() {
        return isEndQueue;
    }

    public void setEndQueue(boolean endQueue) {
        isEndQueue = endQueue;
    }

}
