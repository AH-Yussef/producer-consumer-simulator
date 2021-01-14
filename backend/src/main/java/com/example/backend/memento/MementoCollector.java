package com.example.backend.memento;

import java.util.HashMap;


public class MementoCollector{
    private int secondsCount = 1;
    private HashMap <Integer,Memento> mementoMap = new HashMap<>();

    public void appendToMementoMap(String circuitInfo){
        var newMemento = new Memento(circuitInfo);

        this.mementoMap.put(secondsCount ++, newMemento);
    }

    public String getSpecificMemento(int second){
        return mementoMap.get(second).getMementoInfo();
    }

    public void reset() {
        this.mementoMap.clear();
        this.secondsCount = 1;
    }
}