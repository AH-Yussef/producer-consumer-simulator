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

//    public static void main(String[] args) {
//        Memento str1;
//        str1.setMementoMachineState("shosh 1");
//        Memento str2;
//        str1.setMementoMachineState("shosh 2");
//        Memento str3;
//        str1.setMementoMachineState("shosh 3");
//        Memento str4;
//        str1.setMementoMachineState("shosh 4");
//
//        MementoCollector collector ;
//        collector.appendToMementoMap(str1);
//        collector.appendToMementoMap(str2);
//        collector.appendToMementoMap(str3);
//        collector.appendToMementoMap(str4);
//    }
}